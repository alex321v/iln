package com.legrand.iln;

import java.net.*;
import java.io.*;
import java.util.*;

/* $Id: IrcClient.java,v 1.4.2.1 2004/01/14 16:22:45 legrand Exp legrand $ */

/**
 * Progetto ILN
 * Copyright (C) 2003 Monsieur Legrand
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the license, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 */

/**
 * Classe che implementa il client specifico per la connessione
 * ad un servizio IRC.
 *
 * @author Monsieur Legrand
 * @version 1.4 rev 7
 * @date    7 aprile 2004
 */
public class IrcClient extends GenericClient {
     private Socket s;
     private Analyzer analisi;
     private String motd = new String();
     private InetAddress local;
     private String nickName;
     private String channel;
     private String hostName;
     private BufferedReader in;
     private PrintWriter out;
     private Random casuale;
     private String password = "allanemaechitemmuort";
     private String pass2;
     private long relayMSec = 200;            // tempo di scrittura in msec di un char (by bitslim)
     private String localHost;
     private String localIp;
     private boolean debug = false;
     private String master;               //Contiene il nick del botmaster
     private String tipoResponse;         //Stringa che descrive il tipo di personalita'.
     private boolean autonom = false;     // se true allora interviene autonomamente.
 
     private Hashtable learningTable = new Hashtable(); //Mantiene le tabelle di learning, in modo da fare
                                                //piu' learning in thread paralleli.

     private String lastRes="";            //risoluzione del bug del loop tra due ILN. soluzione PARZIALE. 
     /**
      * Costruttore.
      * Il nome del file di log sarà Irc-&lt;nickname&gt;.
      *
      * @param aHost Il nome dell'host al quale connettersi
      * @param aPort Porta dell'host remoto alla quale connettersi
      * @param aNick Nickname
      * @param aChan Nome del canale al quale effettuare una /JOIN
      * @param deb   True se si vuole il log esteso. False altrimenti.
      * @param aResponse Il tipo di personalità desiderata.
      */
     public IrcClient(String aHost, int aPort, String aNick, String aChan, 
                      boolean deb, String aMaster, String aPass){
	//Il nome del file di log sara' Irc-<nickname>
	super("Irc-" + aNick, aHost, aPort);
	hostName = aHost;
	nickName = aNick;
	channel = aChan;
	master = aMaster;
	debug = deb;
	pass2 = aPass;
	casuale = new Random();
	analisi = new Analyzer(nickName, debug);
	//Eventuale interfaccia grafica. Forse un giorno si fara'.
	try {
	   s = connect();
	} catch (Exception ex){
		System.out.println("[local:] - Impossibile connettersi");
		System.out.println("[local:] - " + ex.getMessage());
		ex.printStackTrace();
		System.exit(0);
	}
	if (!autenticate()) {
		System.out.println("[local:] - Impossibile autenticarsi");
		System.exit(0);
	}
	gestione(s);
     }

     /**
      * Fa partire il client.<BR>
      * Uso: 
      * <code>java IrcClient nome_host numero_porta nick canale botMaster debug_level</code><br>
      * dove nome_host e' l'host al quale connettersi, 
      * numero_porta e' il numero della porta alla quale connettersi, nick
      * e' il nickname da assumere,
      * debug_level e' il livello di debug e botMaster il nick di chi ha il controllo del 
      * programma.
      */ 
      public static void main(String[] args)  {
	boolean ldeb;
	String pass = new String("nopass");
	if (args.length < 6) 
		misuse();
	String host = args[0];
	int porta = Integer.parseInt(args[1]);
	String nick = args[2];
	String chan = "#" + args[3];
	String botMaster = args[4];
	int aDeb = Integer.parseInt(args[5]);
	if (aDeb == 1)
	     ldeb = true;
	else ldeb = false;
	if (args.length > 6) 
		pass = args[6];
	new IrcClient(host, porta, nick, chan, ldeb, botMaster, pass);
      }

      /**
       * Funzione invocata se il programma viene fatto partire con un numero
       * insufficiente di argomenti.
       */
      private static void misuse(){
	  System.out.println("[local:] - Uso: java IrcClient <host> <porta> <nick> <chan> <botmaster> <debug> [pass]");
	  System.out.println("[local:] -       debug = 1 - più messaggi in log. debug = 0 - meno messaggi");
	  System.exit(0);
      }

      /**
       * Gestore del protocollo IRC.
       * 
       * @param sock Un socket aperto e connesso all'Host dell'IRCServer. E' il
       *             Socket restituito dal metodo connect ereditato da GenericClient.
       */
      public void gestione(Socket sock){
	        if (joining())      //Se la join va a buon fine...
	             cicloClient(); //... entra nel ciclo che gestisce la partecipazione al canale.
	        else {
	             System.out.println("[local:] - Impossibile entrare nel canale");
	             try {
	               sock.close();
	             }catch (IOException ex) {
	             	System.out.println("[local:] - Non si chiude il socket: " + ex.getMessage());
	             	ex.printStackTrace();
	             }
	             System.exit(0);
	        }
	}

        /**
	 * Autenticazione sul IRC-Server.<BR>
	 * E' un override del metodo autenticate() della classe GenericClient.
	 *
	 * @return True se l'autenticazione va a buon fine. False altrimenti.
	 */
	public boolean autenticate() {
	   boolean risposta = true;
	   IrcServerMessage lineMess; //Linea di testo proveniente dal server. Incapsulata in una
	                              //classe che rappresenta il protocollo IRC.
	   local = s.getLocalAddress();
	   localHost = local.getHostName();
	   localIp = local.getHostAddress();
	   try{
	      //Costruzione dei buffer di lettura e scrittura con il server IRC.
	      in = new BufferedReader
				  (new InputStreamReader(s.getInputStream()));
	      out = new PrintWriter
				  (s.getOutputStream(), true);

	      //Implementazione del protocollo di autenticazione IRC

	      //send su out PASS command
	      out.println("PASS " + password);
	      System.out.println("[Client:] - PASS " + password);

	      //send su out NickMessage
	      out.println("NICK " + nickName);
	      log("[Client:] - NICK " + nickName);

	      //send su out User Message
	      out.println("USER " + nickName + " " + localHost + " " + hostName + " :" + nickName);
	      log("[Client:] - USER " + nickName + " " + localHost + " " + hostName + " :" + nickName);
	   } catch (Exception ex) {
	       System.out.println("[local:] - " + ex.getMessage());
	       ex.printStackTrace();
	       risposta = false;
	   }
	   waitForWelcome(); //Necessario per compensare l'eventuale net-lag del server.
	   if (!(pass2.equals("nopass")))
	   	out.println("PRIVMSG NickServ :" + "IDENTIFY " + pass2);
	   return risposta;
	}

	/**
	 * Ingresso nel canale, come se si trattasse di un qualunque chatter umano.
	 *
	 * @return true se la /JOIN va a buon fine. False altrimenti.
	 */
        public boolean joining() {
           boolean risposta = true;
           try {
           	out.println ("JOIN " + channel);
           	log("[Client:] - JOIN " + channel);
           } catch (Exception ex) {
	       System.out.println("[local:]" + ex.getMessage());
	       ex.printStackTrace();
	       risposta = false;
	   }

	   return risposta;
        }

	public void kicking() {
		String risposta = new String();
		int r = casuale.nextInt();
		if (r<0) r = -r;
                int selettore = r % 8;
		String str = new String();
		switch (selettore){
			case 0: risposta = "ma dioporco che kickate a fare!?";
				break;
			case 1: risposta = "ma ti diverti tanto a kickarmi?";
				break;
			case 2: risposta = "mi hai preso per Peregrino su #hackit99 ?";
				break;
			case 3: risposta = "ma che banda di azzeccati...";
				break;
			case 4: risposta = "ora ti senti realizzat*? kickare ti fa sentire potente?";
				break;
			case 5: risposta = "guarda che la violenza di un kick non e' la soluzione.";
				break;
			case 6: risposta = "kickate solo perche' non riuscite a trombare a pecora!";
				break;
			case 7: risposta = "secondo me kickate o per sport o per insoddisfazione sessuale!";
				break;
		}
		out.println("PRIVMSG " + channel + " :" + risposta);
		log("[" + channel + "]<" + nickName + "> " + risposta);
	}	

	/**
	 * Se autenticazione e join sono andati a buon fine, il client e' entrato nel canale
	 * come un qualsiasi umano. Questo metodo gestisce l'attivita' di ricevimento dei
	 * messaggi dal canale o da query ed invia le risposte.
	 */
        public void cicloClient() {
            String line = null;
            String prefix, tredig, comando, params;  //Stringhe che rappresentano gli elementi del
	                                             //protocollo dei messaggi IRC.
	    IrcServerMessage lineMess;
	    motd = analisi.getMotd();
	    log("[local]-motd dopo del getMotd: " + motd);
	    //Se il motd non c'e', viene inviato un messaggio di default\
	    if (motd.length()==0)
		    motd = "Ciao a tutt* :*";
            System.out.println("[local:] - Sono entrato in ciclo client!");
	    //stiamo entrando: saluto al canale!
	    out.println("PRIVMSG " + channel + " :" + motd);
	    log("["+channel+"]<"+nickName+"> " + motd);
	    while(true){ //ciclo infinito
               try{
                 line = in.readLine(); //legge una riga di testo proveniente dal server.
                 if (debug)
                    log("[server]: - " + line);
               } catch(IOException ex){
               	 System.out.println("[local:] - Impossibile leggere dal socket: " + ex.getMessage());
               	 System.exit(1);
               }

	       //gestione del messaggio proveniente dal server.
               lineMess = new IrcServerMessage(line);
               prefix = lineMess.getPrefix();
	       tredig = lineMess.getTreDigit();
	       comando = lineMess.getCommand();
	       params = lineMess.getParameters().toLowerCase();

	       //gestione del protocollo IRC
	       if (tredig.equals(ICostantiIrc.ERR_NOTREGISTERED)){
	              autenticate();
	       }
	       if (comando.equals("PING")){
	          out.println("PONG " + params.substring(params.indexOf(":")+1) + " :" + nickName);
	          System.out.println("PONG " + hostName + " :" + nickName);
	       }
	       //If arriva qualcosa sul socket principale
               // allora testa se contiene Priv_Msg
               // se si, allora chiama trattaPrivMsg
	       // In realtà DCC non è ancora implementata.
	       if ( (comando.equals("PRIVMSG")) && (params.indexOf("DCC") < 0) ){
	         trattaPrivMsg(prefix, params);
		 System.gc();  //rilevato un sospetto memory leak in data 20/01/2004
	       }
	       //Implementazione autorejoin in caso di kick
	       if ( (comando.equals("KICK")) && (params.indexOf(nickName) > -1) ){
	          boolean chisenefotte = joining();
		  if (chisenefotte) kicking();
	       }

            }
        }

	/**
	 * Trattamento di un messaggio-stimolo. Puo' essere o un messaggio
	 * proveniente dal canale o un messaggio proveniente da una query privata.<br>
	 * Distingue tra messaggi ai quali rispondere e messaggi relativi alla fase di
	 * learning. Il learn e' sempre in due passi: individuazione della nuova keyword
	 * e learn di possibili risposte.
	 *
	 * @param prefix Il prefisso dello stimolo. Corrisponde al comando di protocollo IRC.
	 * @param params Parametri del messaggio: mittente, destinatario messaggio.
	 */
        public void trattaPrivMsg(String prefix, String params) {
	    //C'e' bisogno di tokenizzare params perche' devo scandirli tutti!
	    StringTokenizer tok = new StringTokenizer(params, " ");
	    //Acchiappo il primo token, che da protollo IRC e' il destinatario del messaggio.
	    String k = tok.nextToken();
	    String[] response;
	    boolean flaggino = false;
	    boolean command = false; //Di default non si deve generare un comando grammaticale.

	    if (k.equals(channel)){      //messaggi in transito sul canale
		    String toNick = trovaNick(prefix);
		    int index = params.indexOf(":");
		    String msg = params.substring(index+1);
		    log("["+channel+"]<"+toNick+"> " + msg);
	    }
	    if (k.equals(nickName)) {   //Se il destinatario e' proprio il nostro nick....
		                        //e quindi si tratta di query privata!
	    	String toNick = trovaNick(prefix); //trovo il nick del mittente
		int index = params.indexOf(":");
		String msg = params.substring(index+1);
	    	log ("[query-"+toNick+"]<"+toNick+"> " + msg);
		//anti ping flood
		if (msg.indexOf("PING")>0) {
			out.println("PRIVMSG " + toNick + " :cazzo pinghi a fare?");
			log("[query-"+toNick+"]<"+nickName+ "> cazzo pinghi a fare?");
		}
		else //anti time flood
		if (msg.indexOf("TIME")<0)
		if (learningTable.containsKey(toNick)) //Se il mittente mi sta insegnando qualcosa...
	    	   if (((String)learningTable.get(toNick)).equals("begin_learn")) //se il learn e' iniziato
	    	      learn2(toNick, params);    //allora fai il passo 2 del learn
	    	   else
	    	      learnEnd(toNick, params); //altrimenti abbiamo finito il learn!
	    	else {                            //Se il mittente non mi sta insegnando nulla....
	    	    String k2 = tok.nextToken(); //prendo il messaggio
	    	    if (k2.equals(":versione")){    //Se il messaggio e' un comando richiesta versione
	    		response = analisi.version(); //restituisco la versione (e' compito di analisi)
	    		for (int i=0; i<response.length; i++){
	    		    out.println("PRIVMSG " + toNick + " :" + response[i]);
	    		    log("[query-"+toNick+"]<"+nickName+ "> " + response[i]);
	    		}
	            }
	            else if (k2.equals(":chi_sei")){ //se invece e' un comando di identificazione
	        	response = analisi.chisei();         //analisi restituisce l'identificazione.
	        	for (int i=0; i<response.length; i++){
	    		    relay(response[i]);						 	// <-- rallentatore!
			    out.println("PRIVMSG " + toNick + " :" + response[i]);
	    		    log("[query-"+toNick+"]<"+nickName+ "> " + response[i]);
	    		}
	            }
	            else if (k2.substring(1).trim().equals(nickName) || //Se invece mi stanno chiamando...
	                     k2.substring(1).trim().equals(nickName.toLowerCase())){
			relay("Si?"); 								// <-- rallentatore!
	                out.println("PRIVMSG " + toNick + " :Si?");   //...cazzo volete?
	       	        log("[query-"+toNick+"]<"+nickName+ "> " + " Si?");
	       	    }
		    else if (k2.equals(":aut")){  //aggiunto il 10/11/2002 per regolare risposte autonome
			    autonom = true;
			    out.println("PRIVMSG " + toNick + " :Inizio risposte autonome in canale");
			    log("To: " + toNick + " :Inizio risposte autonome in canale");
		    }
		    else if (k2.equals(":noaut")){
			    autonom = false;
		            out.println("PRIVMSG " + toNick + " :Fine risposte autonome in canale");
			    log("To: " + toNick + " :Fine risposte autonome in canale");
		    }                            //Fin qui!
	       	    else if (k2.equals(":stop") && (toNick.equals(master))){   //Se e' il comando di arresto programma...
	       		response = new String[1];   //.... mi arresto e termino senza errori :)
	       		response[0] = "C'è tanto da pariare nella vita... ed io pareo si di voi!";
	       		out.println("PRIVMSG " + toNick + " :Ordine di arresto programma. Ciao a presto");
	       		log("[query-"+toNick+"]<"+nickName+ ">  Ordine di arresto programma. Ciao a presto");
	       		out.println("PRIVMSG " + channel + " :Mi fermo qui. Ciao a tutti");
			log("[" + channel + "] <" + nickName + "> Mi fermo qui. Ciao a tutti");
	       		for (int i = 0; i < 10000; i++);
	       		out.println("PART " + channel + " :" + response[0]);
	       		log("PART " + channel + " " + response[0]);
	       		for (int i = 0; i < 40000; i++);
	       		out.println("QUIT " + response[0]);
	       		log("QUIT " + response[0]);
	       		mLog.close();
	       		try {
	       		    s.close();
	       		}catch (IOException ex){
	       			log("[local:] - Impossibile chiudere il socket!");
	       			log("ex.getMessage()");
	       			ex.printStackTrace();
	       		}
	       		System.exit(0);
	       	    }
	            else {	//se invece il messaggio non e' nulla di quanto sopra, allora occorre
			        //ragionare!!!! invocazione di analisi.generation.
				// a meno che non e' il capo :)
			if (toNick.equals(master) && (params.indexOf("c_p") > 0)){
				int lon = nickName.length() + 6;
				out.println("PRIVMSG " + channel + " :" + params.substring(lon));
				log("[" + channel + "] <" + nickName + "> " + params.substring(lon));
			}
			else {
	       	          response = analisi.generation(toNick, params, 1);
	       	          if (response != null){
	       	           if (response[0].indexOf("CMD") > -1){ //Se la generazione restituisce
				                                 //un comando grammaticale
	       	           	                                 //lo si esegue!
				 response = analisi.esegueComando(response[0], params, 2);
	       	                 command = true;
	       	           }
	       	           if (!response[0].equals("learn")){   //Se la generazione e' andata a buon fine
	       	              for (int i = 0; i < response.length; i++)   //si risponde
	       	                  if ((response[i]!=null) && (!response[i].equals("null"))){
				      relay(response[i]);					//rallentatore!
				      out.println("PRIVMSG " + toNick + " :" + response[i]);
	       	                      log("[query-"+toNick+"]<"+nickName+"> " + response[i]);
				  }
	       	              if (command){   //reset post esecuzione comando ;)
	       	              	command = false;
	       	              }
	       	           }
	       	           else {      //Se la generazione NON e' andata a buon fine, si entra in stato di learning.
	       	     	       learningTable.put(toNick, "begin_learn");
	       	     	       out.println("PRIVMSG " + toNick + " :Non so cosa significhi. La frase chiave quale e'?");
	                       log("[query-"+toNick+"]<"+nickName+"> Non so cosa significhi. La frase chiave quale e'?");
	       	           }
	       	          }
			}
	       	    }
	        }
	    } //Fin qui il trattamento delle query. Ora il trattamento di messaggi in chan.
	    else if (k.equals(channel)){
	       String k2 = tok.nextToken(); //acchiappo il token successivo, che e' la prima parola
	                                    //del messaggio.
	       if (!tok.hasMoreTokens() &&  //se qualcuno mi sta chiamando....
	           (k2.substring(1).trim().equals(nickName) ||
	            k2.substring(1).trim().equals(nickName.toLowerCase()))){
		       String risposta_x = new String();
		       int r_x = casuale.nextInt();
		       if (r_x<0) r_x = -r_x;
		       int selettore_x = r_x % 8;
		       String str_x = new String();
		       switch (selettore_x){
			       case 0: risposta_x = "Si?";
				       break;
			       case 1: risposta_x = "che c'e'?";
				       break;
			       case 2: risposta_x = "dimmi";
			               break;
			       case 3: risposta_x = "ja?";
		                       break;
			       case 4: risposta_x = "cosa c'e'?";
			               break;
			       case 5: risposta_x = "che cazzo vuoi?";
			               break;
			       case 6: risposta_x = "parla, ti ascolto";
				       break;
			       case 7: risposta_x = "beh?";
		                        break;
		       }
		       relay(risposta_x);         //rallentatore
		       out.println("PRIVMSG " + channel + " :"+risposta_x); //...cazzo vuoi?
		       log("[" + channel + "]<" + nickName + "> "+risposta_x);
	       }
	       /* Questa parte e' certamente da migliorare! */
	       else {                       //Altrimenti....
	       	 tok = new StringTokenizer(params, " ");
	         while (tok.hasMoreTokens() && !flaggino) {       //... scandisco il messaggio per capire
	       	    String parola = tok.nextToken(); //se ce l'hanno con me o no :)
	       	    if (parola.indexOf(nickName) > -1) {  //se ce l'hanno con me....
		       flaggino = true;
	       	       response = analisi.generation(params, 1); //chiamo il generatore!
		       log ("tornato dal generatore con resp --> " + response[0]);
	       	       if ((!response[0].equals("no_response")) && (!response[0].equals(lastRes))){  // fix del loop bug!
			     //log ("sono nel if anti flood");
			     if (response[0].indexOf("CMD")>-1) {
				     log ("riconosciuto comando");
				     response=analisi.esegueComando(response[0], params, 1);
				     log("tornato da esegueComando con risposta --> " + response[0]);
				     command = true;
			     }
			     lastRes = response[0]; 
			     for (int i = 0; i < response.length; i++){ 
			        relay(response[i]);
				out.println("PRIVMSG " + channel + " :" + response[i]);
				log("[" + channel + "]<" + nickName + "> " + response[i]); //... No learning in canale!
			        if (command) command = false;
			     }
	       	       }
		       else {
			     if (autonom){
			        int r = casuale.nextInt();
			        if (r<0) r = -r;
			        int selettore = r % 7;
			        String str = new String();
			        switch (selettore){
				 case 0: str="non capisco";
				         break;
				 case 1: str="ma che fesserie dici...";
				         break;
				 case 2: str="stai dicendo una cosa che non comprendo";
				         break;
				 case 3: str="spiegati meglio per cortesia";
				         break;
				 case 4: str="non capisco, e' senza senso...";
				         break;
			         case 5: str="secondo me tu stai male, molto male";
					 break;
				 case 6: str="uhm... noto che sei bravo a dire cose senza senso e senza semantica";
					 break;
				 }
		                relay(str);							//rallentatore!		
			        out.println("PRIVMSG " + channel + " :" + str);
				log("[" + channel + "]<" + nickName + "> " + str);
			     }
		       }
	       	    }

	         } //end while
		 if (!flaggino && autonom){
			 //sviluppo algoritmo di intervento autonomo
			 flaggino = true;
			 response = analisi.generation(params, 1);//chiamo il generatore
			 if (!response[0].equals("no_response") && !response[0].equals(lastRes)){ //se ho risposte le do, se no silenzio
				 if (response[0].indexOf("CMD") > -1){
					 response=analisi.esegueComando(response[0], params, 1);
					 command = true;
				 }
				 lastRes = response[0];
				 relay(response[0]);						//rallentatore
				 out.println("PRIVMSG " + channel + " :" + response[0]);
				 log("[" + channel + "]<" + nickName + "> " + response[0]);
				 if (command) command = false;
			 }
		 }
	       }
	    }
	    else {  //attenzione: punto critico. loggare per debuggare. Forse un giorno mostreremo che è inutile!
		    log("Fr: " + prefix + " " + params); //si si ci sono quasi al fatto che e' inutile :)
            }
	}
 
	/**
	 * Routine di compensazione del net-lag.<br>
	 * Segue alla lettera il protocollo IRC finche' il server
	 * non da il WELCOME.
	 */
        private void waitForWelcome() {
           String line = null;
           String tredig, comando;
           IrcServerMessage lineMess;
           boolean flag = true;
           while(flag){
               try{
                 line = in.readLine();
               } catch(IOException ex){
               	 log("[local:] - Impossibile leggere dal socket: " + ex.getMessage());
               }
               log("[Server:]" + line);
               lineMess = new IrcServerMessage(line);
	       tredig = lineMess.getTreDigit();
	       comando = lineMess.getCommand();
	       if (comando.equals("PING")){
	          out.println("PONG " + hostName + " :" + lineMess.getParameters()); //the_nihilant
	          System.out.println("PONG " + hostName + " :" + lineMess.getParameters());
	       }
	       if (tredig.equals(ICostantiIrc.ERR_NOTREGISTERED)){
	              autenticate();
	       }
	       if(comando.equals("PRIVMSG")){
	       	   String toNick = trovaNick(lineMess.getPrefix());
	       	   out.println("NOTICE " + toNick + " :nospoof now");
	       	   flag = false;
	       	   log("NOTICE " + toNick + " :nospoof now");
	           out.println("PRIVMSG " + " " + toNick + " :IlnIRC VERSION 1.4");
	           log("PRIVMSG " + " " + toNick + " :IlnIrc VERSION 1.4");

	           out.println("PONG " + hostName + " :" + nickName);
	           System.out.println("PONG " + hostName + " :" + nickName);

	           autenticate();
	           joining();
	       }
	       if (tredig.equals(ICostantiIrc.RPL_WELCOME))
	            flag = false;
	       	 
            }
        }

	/**
 	 * Utility per rallentare la risposta.<br>
	 * by bitslim.
	 *
	 * @param stringa Il messaggio da rallentare
	 **/

	private void relay(String aString) {
	  long writeTime = aString.length() * relayMSec;
	  long startTime = System.currentTimeMillis();

	  while( (System.currentTimeMillis() - startTime) < writeTime ) { /* nn fa nulla*/ }
	}
								    
        /** 
	 * Utility per tirare fuori il nick del mittente da un messaggio IRC.
	 *
	 * @param stringa Il messaggio IRC.
	 * @return Il nick del mittente.
	 */
        private String trovaNick(String stringa){
         StringBuffer rispostina = new StringBuffer();
         int i = 1;
         while (stringa.charAt(i) != '!'){
            rispostina.append(stringa.charAt(i));
            i++;
            }

         return rispostina.toString();

        }

	/**
	 * Passo 2 di learning
	 *
	 * @param toNick insegnante
	 * @param params frase detta dall'insegnante.
	 */
        private void learn2(String toNick, String params) {
           int k = params.indexOf(":");
           String newKeyWord   = params.substring(k+1).trim();
           if (newKeyWord.indexOf(nickName) > -1 ){ //okkio! Stanno tentando l'hack del nome!
		   out.println("PRIVMSG " + toNick + " :No no non puoi darmi come chiave qualcosa con il mio nome...");
		   out.println("PRIVMSG " + toNick + " :poi va a finire che quando la gente mi invoca io rispondo cosi");
		   out.println("PRIVMSG " + toNick + " :e mica ci casco... sce'!");
		   learningTable.remove(toNick);
		   log("[query-" + toNick + "]<"+nickName+ "> No no non puoi darmi come chiave qualcosa con il mio nome...");
		   log("[query-" + toNick + "]<"+nickName+ "> poi va a finire che quando la gente mi invoca io rispondo cosi");
		   log("[query-" + toNick + "]<"+nickName+ "> e mica ci casco... sce'!");
	   }
	   else {
	      learningTable.put(toNick, newKeyWord);
              out.println("PRIVMSG " + toNick + " :" + newKeyWord + ". Mi dici una bella frase che riguarda ciò? Io la imparo.");
	      log("[query-" + toNick + "]<"+nickName+"> " + newKeyWord + ". Mi dici una bella frase che riguarda ciò? Io la imparo.");
           }
	}

	/**
	 * Termine del learning. Invoca l'analizzatore per fissare ciò che si e' imparato.
	 *
	 * @param toNick  insegnante
	 * @param params  frase detta dall'insegnante.
	 */
        private void learnEnd(String toNick, String params){
            int k = params.indexOf(":");
            String newPhraseLearned = params.substring(k+1).trim();
            String newKeyWord   = (String)learningTable.get(toNick);
	    //Controllo bug di gi0
	    if (newKeyWord.equals("") || newPhraseLearned.equals("") ||
		newKeyWord.equals("bestemmia") || newKeyWord.equals("aut") ||
   		newKeyWord.equals("noaut") ){ //controllo hack comandi
		out.println("PRIVMSG " + toNick + " :Ehi mica vorrai fregarmi con qualche stupido hack... sei un lamer :))");
	        log("[query-" + toNick + "]<"+nickName+ "> Ehi mica vorrai fregarmi con qualche stupido hack... sei un lamer :))");
	    }
	    else {
	    	out.println("PRIVMSG " + toNick + " :Ho capito: " + newKeyWord + " " + newPhraseLearned);
	        log("[query-" + toNick + "]<"+nickName+"> Ho capito: " + newKeyWord + " - " + newPhraseLearned);
	        out.println("PRIVMSG " + toNick + " :Grazie davvero. Non si finisce mai di imparare :-)");
                log("[query-" + toNick + "]<"+nickName+ "> Grazie davvero. Non si finisce mai di imparare :-)");
                log("Entro in analisi.learing con parametri: " + newKeyWord + " " + newPhraseLearned);
		analisi.learning(newKeyWord, newPhraseLearned);
	    }
            learningTable.remove(toNick);
        }

}
