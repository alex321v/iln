package com.legrand.iln;

import java.io.*;
import java.util.*;
import java.net.*;


/**
 * Progetto ILN
 * Copyright (C) 2003-2017 Monsieur Legrand
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
 * Classe generatrice di risposte.<br>
 * E' suo compito l'esaminare la base di conoscenza ed
 * estrarre l'opportuna reazione agli stimoli ricevuti.<br>
 * La versione 1.3 (e successive) presenta la novità, rispetto alla 1.2,
 * di implementare la ricerca ricorsiva all'interno della base di
 * conoscenza. <br>
 *
 * @author  Monsieur Legrand
 * @version 1.5 rev 1
 * @date    04 agosto 2017 
 */

public class Risposte implements Animatore
{
    
    /** Vector contenente i nicknames noti.
    */
    Vector nomi;

    /** Vector per funzionalita' comando imp.
    */
    Vector imparate;

    /** Flag per testare se e' la prima cosa che viene detta al programma
        in una query.
    */
    boolean first = true;
    
    /** HashTable contenente tutta la conoscenza dell'ILN
    */
    Hashtable struttura;
    
    /** Vector usato per "fare conoscenza" con un nuovo nick non noto
     */
    Vector handShacking;
    
    /** Oggetto che serve per la generazione di numeri casuali.
        L'algoritmo di generazione di numeri casuali mi ha molto deluso,
        pertanto credo che cerchero' un nuovo algoritmo.
    */
    Random casuale;
    
    /** Oggetto necessario per le funzioni di logging
    */
    LogServer log;
    
    /** Stringa contenente il nickName dell'ILN
    */
    String nickName;
    
    /** Numero intero che serve, quando si scandiscono i parametri,
        a indicare l'inizio del messaggio vero e proprio.
    */
    int startMess;
    
    /** Flag di controllo livello di debug
    */
    boolean debug;
    
    /** Stringa contenente il motd
    */
    String motd;


    /**
     * Costruttore.<br>
     *
     * @param nick     nickname proprio
     * @param deb      booleano. True = debugMode, False altrimenti
     */

    public Risposte(String nick, boolean deb) {
    	        nickName = nick;
    	        startMess = 2 + nick.length();
    	        nomi     = new Vector();
		debug = deb;
    	        if (debug)
    	           log = new LogServer("strutt");

    	        Collection transit = RobotUtils.leggeDati("./resources/" + nickName + "nomi");
		int i = 0;
		Iterator it = transit.iterator();
                //carica tutti i nomi/nickname già noti
		while (it.hasNext()) {
			nomi.add((String)it.next());
			i++;
	        }
    	        leggeStruttura(nickName);
    	        handShacking = new Vector();
		imparate = new Vector();
		casuale = new Random();
    }

    /**
     * Rende leggibile dall'esterno il motd.
     * <br>
     * @return una singola stringa contenente il motd.
     */

    public String getMotd() {return motd;}

    /**
     * Restituisce informazioni sulla versione
     */
    public String[] version(){
	String[] risposta = {"************* Begin Info ****************",
		            "ILN 1.5.1 beta in collaudo. AL MOMENTO INSTABILE",
		            "Semplice riconoscitore automatico sperimentale di testi",
		            "Compilato il 05 agosto 2017",
		            "A cura di Monsieur Legrand",
		            "Automa in puro Java al 100% alla faccia di Micro$oft e all'anima di Bill Gates.",
		            "tutto rilasciato sotto licenza GPL. Il codice sorgente e' disponibile per gli sviluppatori",
			    "alla URL http://iln.ppcnerds.org/",
		            "************* End Info ******************" };
       return risposta;
    }

    /**
     * Restituisce una autodescrizione
     */
    public String[] chisono() {
	String[] risposta = {"Sono un automa a stati finiti non deterministico",
		             "che sta imparando a parlare con le persone."};

        return risposta;
    }

    /**
     * Generazione di una risposta ad uno stimolo proveniente da una query privata.
     * <br>
     *
     * @param nick      Nickname di chi effettua la query.
     * @param keyPhrase Frase-stimolo scritto in query.
     * @param howMuch   Intero che indica il numero di righe della risposta.
     *
     * @return La risposta allo stimolo.
     */
    public String[] generation(String nick, String keyPhrase, int howMuch){
	String[] risposta = new String[howMuch];
	if (!nomi.contains(nick)){
	     risposta[0] = nick + ": non ti conosco. Ciao, chi sei?";
	     nomi.add(nick);
	     RobotUtils.aggiungiNome(nickName + "nomi", nick);
	     handShacking.add(nick);
	}
	//testiamo se e' la prima cosa che viene detta al programma
	else if (first) {
		risposta[0] = "Ciao " + nick + ". Come va?";
		first = false;
	     }
	     //testiamo se e' un nome nuovo
	     else if (handShacking.contains(nick)){
	     	 risposta = new String[1];
	     	 risposta[0] = "Piacere di conoscerti " + nick;
	     	 handShacking.remove(nick);
	     }
	     else {
	         risposta = generatore(keyPhrase.trim(), howMuch);
	     	 //se il programma non ha risposte buone da dare, allora va in
		 //modalità learning
		 if (risposta[0].equals("no_response")){
	     	      risposta = new String[1];
	     	      risposta[0] = "learn";
	     	 }
	     }
	return risposta;
    }

    /**
     * Generazione di una risposta ad uno stimolo proveniente dal canale IRC.<br>
     *
     * @param keyPhrase Frase-stimolo scritto in canale.
     * @param howMuch   Intero che indica il numero di righe della risposta.
     *
     * @return La risposta allo stimolo
     */

    public String[] generation(String keyPhrase, int howMuch){
	        if (debug)
	             log.logga ("entrato in generation con keyPhrase = " + keyPhrase);
	        String[] risposta = generatore(keyPhrase.trim(), howMuch);

	        return risposta;
    }

    /**
     * Funzione di pilotaggio del learning.<br>
     * Inserisce nella base di conoscenza cio' che si e' imparato.
     *
     * @param newKey    Nuova chiave da inserire
     * @param newPhrase Nuova frase da dare in risposta allo stimolo-chiave
     */
    public void learning(String newKey, String newPhrase) {
    	Vector v = new Vector();
	
	//controllo esistenza chiave. Risoluzione bug 21 febbraio 2004
	if (struttura.containsKey(newKey.toLowerCase())) {
		//leggere prima quel che c'e' dentro e poi appenderci la nuova!
		v = (Vector)struttura.get(newKey.toLowerCase());

	}
        v.add(newPhrase);	
	struttura.put(newKey.toLowerCase(), v);
    	String salvataggio = newPhrase.toLowerCase() + " --- " + newKey.toLowerCase();
	String imparata = newKey.toLowerCase() + " --> " + newPhrase.toLowerCase();
    	imparate.add(imparata);
	//log.logga ("scrivo nel file + " + salvataggio);
	RobotUtils.aggiungiNome(nickName, salvataggio);
	//log.logga ("ho scritto nel file " + salvataggio);
    }


    /**
     * Funzione centrale del kernel del cervello.<br>
     * Trova la chiave in un messaggio.<br>
     * Funzione ancora in corso di sviluppo. Forse lo sviluppo sara' perenne.
     * Per ora viene trovata solo la prima chiave incontrata nel messaggio.
     * Se in corrispondenza di questa chiave vengono trovate risposte multiple,
     * la risposta da dare verra' selezionata secondo l'attuale algoritmo di
     * selezione.<p>
     *
     * @param aMess  Messaggio da esaminare
     *
     * @return Chiave trovata.
     */

    private String trovaKey(String aMes){ //da migliorare!
    	String risposta = new String("empty_no_key");
	Vector risposteTrovate = new Vector();  //per gestione chiavi multiple
    	boolean flag = true;
	String messaggio;

    	if (aMes.indexOf("CMD_rec") < 0)
	    messaggio = aMes.substring(aMes.indexOf(" :") + 1);
	else messaggio = aMes;

    	Set chiavi = struttura.keySet();
    	if ( (chiavi != null) && (chiavi.size() != 0) ){
    		Iterator it = chiavi.iterator();
    		while (it.hasNext()){
    	           String parola = (String)(it.next());
    	           if (messaggio.indexOf(parola) > -1){
    			//primo caso: messaggio == chiave
    			if (messaggio.trim().equals(parola))
    			        risposteTrovate.add(parola);
    			else
    			//secondo caso: chiave su bordo del messaggio
                           if (
                              ((messaggio.indexOf(parola) == 0) && (validateKey(messaggio,parola.length())) )
    			       ||
    			      ((messaggio.length() == messaggio.indexOf(parola) + parola.length()) &&
    			          (validateKey(messaggio, messaggio.indexOf(parola)-1) ) )
    			      )
    			              risposteTrovate.add(parola);
    			   else {
    			   //terzo caso: chiave dentro messaggio
    			        int fineKey = messaggio.indexOf(parola) + parola.length();
    			        if (messaggio.indexOf(parola) > 0 && messaggio.length()> fineKey)
    			          if ( (validateKey(messaggio, messaggio.indexOf(parola)-1)) &&
    			               (validateKey(messaggio,fineKey)) )
    			                risposteTrovate.add(parola);
    			   }
    		 }
    		}
    	}
	if (risposteTrovate.size()>0){
	    int selettore = selRandom(10);
	    //l'attuale algoritmo di selezione prevede la scelta casuale non 
	    //uniforme tra tre diversi criteri semplici di selezione.
	    switch (selettore) {
	      case 0: 
		// selezione casuale tra chiavi multiple
		int k = selRandom(risposteTrovate.size());
		risposta = (String)risposteTrovate.get(k);
		break;
	      case 1:
	      case 2:
	      case 3:
	      case 4:
	      case 5:
	      case 6:
	      case 7:
		//selezione della piu' lunga tra le chiavi trovate
		//e il criterio con probabilita' di selezione piu' alta
		risposta = (String)risposteTrovate.get(0);
		for (int i = 1; i < risposteTrovate.size(); i++){
			String transit = (String)risposteTrovate.get(i);
			if (transit.length() > risposta.length())
				risposta = transit;
		}
		break;
	      case 8:
	      case 9:
		//altre implementazioni alternative: 16/03/2002
		//I. Selezione secondo ordine lessicografico	
		risposta = (String)risposteTrovate.get(0);
		for (int i=1; i< risposteTrovate.size(); i++){
			String transit = (String)risposteTrovate.get(i);
			if (transit.compareTo(risposta) < -1)
                               risposta= transit;
		}
		break;
	    }
	}

    	return risposta;
    }

    private boolean validateKey(String messaggio, int offset){
	//test sui delimitatori della stringa key
    	boolean risposta = false;
    	if ( (messaggio.charAt(offset) == ' ') ||
	     (messaggio.charAt(offset) == '\'') ||
    	     (messaggio.charAt(offset) == '!') ||
    	     (messaggio.charAt(offset) == '?') ||
    	     (messaggio.charAt(offset) == '.') ||
    	     (messaggio.charAt(offset) == ',') ||
    	     (messaggio.charAt(offset) == ';') ||
    	     (messaggio.charAt(offset) == '"') ||
    	     (messaggio.charAt(offset) == ':') )
	         risposta = true;
	return risposta;
   }


    /**
     * Genera la risposta. E' compito di questa funzione l'invocare
     * @see trovaKey ed estrarre la risposta corrispondente.
     * <br>
     *
     * @param keyPhrase Messaggio-Stimolo.
     * @param howMuch   Numero di righe della risposta.
     *
     * @return Risposta allo stimolo.
     */

    private String[] generatore(String keyPhrase, int howMuch){
    	String[] risposta = new String[howMuch];
	//log.logga("Entro in trovakey con keyPhrase = " + keyPhrase);
    	String presentKey = trovaKey(keyPhrase);
	//log.logga("trovata key --> " + presentKey);
	if (!presentKey.equals("empty_no_key")){
	     Vector frasi = (Vector)struttura.get(presentKey);
	     Vector frasiPrese = new Vector();
	     int i = 0;
	     //log.logga("Entro nel while con howMuch = " + howMuch);
	     while (i < howMuch){
	     	  int k = selRandom(frasi.size());
	     	  Integer k1 = new Integer(k);
	     	  if (!frasiPrese.contains(k1)){
	     	           frasiPrese.add(k1);
			   if (((String)frasi.get(k)).indexOf("REC") > -1){
				if (debug)
				   System.out.println("Chiamata REC! parametro --> " + ((String)(frasi.get(k))).substring(4));
			        String[] intermediate = generatore(nickName + " :"+((String)frasi.get(k)).substring(4), 1);
                                risposta[i] = intermediate[0];
				if (debug)
				     System.out.println("Tornano da chiamata REC con risposta --> " + intermediate[0]);
			   }
			   else risposta[i] = (String)frasi.get(k);
	     	           i++;
	     	  }
	     }
	 }
	 else risposta[0] = "no_response";
	 if (debug){
	     log.logga("Frase passata: " + keyPhrase);
	     log.logga("Chiave trovata: " + presentKey);
	     log.logga("Valori in risposta: ");
	     for (int i=0; i < howMuch; i++) log.logga(risposta[i]);
         }
	 //controllo ricorsione grammaticale
	 for (int count=0; count < risposta.length; count++)
		 if (risposta[count].indexOf("CMD_rec") > -1){
			 int posiz = keyPhrase.indexOf(presentKey) + presentKey.length() + 1;
			 risposta[count] += " " + new String((new Integer(posiz)).toString());
		 }
	 return risposta;
    }

    /**
     * Se la risposta allo stimolo e' un comando della grammatica interna,
     * viene invocato dall'esterno questo metodo, che esegue il comando.
     *
     * @param comando   Comando grammaticale da eseguire
     * @param keyPhrase Messaggio stimolo. Viene passato in modo che possa funzionare come
     *                  argomento del comando
     * @param howMuch   Numero di righe da generare come risposta.
     *
     * @return Risposta allo stimolo.
     */
    public String[] esegueComando(String comando, String keyPhrase, int howMuch) {
    	String cmd = comando.substring(4);
    	String[] risposta = new String[1]; 

    	if (cmd.equals("nome")) {
	      risposta = new String[2];
    	      risposta[0] = "Mi chiamo " + nickName + " perchè, non lo sai?";
	      risposta[1] = "Se non lo sai peggio per te... ma ora lo sai :)";
    	}
    	if (cmd.equals("iast")) {
    	      Iastemmiatore iast = new Iastemmiatore();
	      if (debug)
		      log.logga("TRACE: chiamata a iast.generation("+keyPhrase+", "+howMuch+")");
    	      risposta = iast.generation(keyPhrase, howMuch+1);
	      if (debug)
	              log.logga("TRACE: ritorno da iast.generation, risposta[0] = " + risposta[0]);
    	}
    	if (cmd.equals("sex")) {
		risposta = new String[2];
    		risposta[0] = "Ma perchè voi esseri umani finite sempre con il parlare di sesso?";
    		risposta[1] = "Sarete mica tutti pervertiti... :)";
        }
	if (cmd.indexOf("rec") > -1) {  //ricorsione grammaticale!
		//se si tratta di un CMD_rec, dopo la stringa di comando e' stato aggiunto
		//l'indice in cui termina la key che ha generato la ricorsione. 
		//Il formato della stringa e' CMD_rec n
		//pertanto e' da rielaborare la sottostringa di params da n+1 in poi.
		int recursiveIndex = Integer.parseInt(comando.substring(8));
		String newParams = nickName + " :" + keyPhrase.substring(recursiveIndex);
		log.logga("chiamo generation con newParams = " +  newParams);
		risposta = generation(newParams, 1);  //pezza da sistemare
	}
	if (cmd.indexOf("imp") > -1) {  //elenco cose imparate
		if (imparate.size() != 0){
		    risposta = new String[imparate.size()+1];
		    risposta[0] = "oggi ho imparato:";
		    for (int i = 1; i <= imparate.size(); i++)
			    risposta[i] = (String)imparate.get(i-1);
		}
		else {
			risposta[0] = "non ho imparato ancora nulla oggi :(";
		}
	}
		
    	return risposta;

    }


    /**
     * Utility che serve a creare lo stato iniziale, caricando tutta
     * la struttura della base di conoscenza.<br>
     * Questo metodo (privato) viene invocato dal costruttore.<br>
     *
     * @param fileName Nome del file contenente la base di conoscenza.
     */
    private void leggeStruttura(String fileName){
    	struttura = new Hashtable();
        motd = leggeMotd();
	caricamento ("./resources/base", struttura);
	caricamento ("./resources/" + fileName, struttura);
    }

    /**
     * Utility privata che ha il compito di leggere il motd dal
     * file su disco.<br>
     * Attenzione: il motd deve essere per forza scritto su un
     * rigo solo.<br>
     *
     * @return una String contenete il motd.
     */

    private String leggeMotd(){
	    String risposta = new String();
	    Collection raw = RobotUtils.leggeDati("./resources/motd");
	    if (raw != null && raw.size()!=0){
		    Iterator it = raw.iterator();
		    risposta = (String)it.next();
	    }
	    return risposta;
    }
	       

    /**
     * Funzione privata di caricamento di un file
     *
     * @param fileName Nome del file da caricare, senza estensione: viene aggiunta .txt automaticamente
     * @param struttura la Hashtable nella quale caricare quanto letto dal file.
     */
    private void caricamento(String fileName, Hashtable struttura){
	Collection raw = RobotUtils.leggeDati(fileName);
    	if ( (raw != null) && (raw.size() != 0) ) {
    		Iterator it = raw.iterator();
    		Vector puntati;
		int ind = 1;
    		while (it.hasNext()) {
    			String estratta = (String)it.next();
			if (debug)
			   System.out.println("estratta: " + estratta);
			ind++;
			MyTokenizer token = new MyTokenizer(estratta, " --- ");
    			String frase = token.nextToken();
    			String keyPresa = token.nextToken().toLowerCase();
    			if (struttura.containsKey(keyPresa))
    			     puntati = (Vector)struttura.get(keyPresa);
    			else puntati = new Vector();
    			puntati.add(frase);
    			struttura.put(keyPresa, puntati);
    		}
    	}
    }

    /**
     * Utility privata per la selezione di un numero intero casuale.
     *
     * @param max estremo superiore del casuale da estrarre.
     *
     * @return L'intero estratto.
     */
    private int selRandom(int max) {
	int r = casuale.nextInt();
	if (r<0) r = -r;
	return r % max;
    }

    /**
     * Utility privata usata in fase di debug per stampare su schermo
     * tutta la struttura di conoscenza caricata da file alla partenza.
     */
    private void stampaStruttura() {
    	System.out.println("<-- Inizio logging struttura -->");
    	Set chiavi = struttura.keySet();
    	int i = 1;
    	Iterator it = chiavi.iterator();
    	while (it.hasNext()){
    		String chiave = (String)it.next();
    		Vector valori = (Vector)struttura.get(chiave);
    		System.out.println("chiave " + i + ": " + chiave + " valori: " + valori.toString());
    		
    	}
    	System.out.println("<-- Fine logging struttura -->");
    }

}
