package com.legrand.iln;

import java.io.*;
import java.util.*;

/* $Id: TestClient.java,v 1.4.2.1 2004/01/14 16:24:12 legrand Exp legrand $ */

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
 * @version 1.4 rev 3
 * @date   08 aprile 2004
 */

public class TestClient {
     String nickName;
     boolean debug = false;
     String motd = new String();
     Analyzer analisi;
     String tipoResponse;
     String visitor;
     BufferedReader in;

     Hashtable learningTable = new Hashtable();

     /**
      * Costruttore. Viene invocato automaticamente alla partenza del programma.
      * Non c'e' bisogno di effettuare chiamate esplicite.
      *
      * @param aNick nickname dell'istanza del programma
      * @param deb   un booleano che indica il livello di debug: 
      *              true = debug alto, false = debug basso.
      * @param aResponse una stringa contenente la personalita' dell'istanza del programma.
      */
     public TestClient(String aNick, boolean deb){
	nickName = aNick;
	debug = deb;
	in = new BufferedReader(new InputStreamReader(System.in));
	analisi = new Analyzer(nickName, debug);
	//Eventuale interfaccia grafica
	System.out.print("chi sei? ");
	try{
	  String line = in.readLine();
	  visitor = ":" + line + "!localhost";
	} catch (IOException ex){
		System.out.println("Errore di I/O: " + ex.getMessage());
		ex.printStackTrace();
		System.exit(1);
        }
	cicloClient();
     }
 
     /**
      * Fa partire il client.<BR>
      * Uso: 
      * <code>java TestClient nick debugLevel</code><br>
      */ 
      public static void main(String[] args)  {
	boolean ldeb;
	if (args.length < 2)
		misuse();
	String nick = args[0];
	int aDeb = Integer.parseInt(args[1]);
	if (aDeb == 1)
	     ldeb = true;
	else ldeb = false;

        new TestClient(nick, ldeb);
      }

      /**
       * Funzione di utilita' invocata se il programma viene fatto partire con un numero
       * insufficiente di argomenti.
       */
      private static void misuse(){
	  System.out.println("[local:] - Uso: java TestClient <nick> <debug> ");
	  System.out.println("[local:] -       debug = 1 - più messaggi in log. debug = 0 - meno messaggi");
	  System.exit(0);
      }

      /**
       * Funzione principale del client locale, contiene il ciclo da eseguire durante un dialogo.
       */
      public void cicloClient() {
            String line = null;
            String prefix, tredig, comando, params;
            String[] response;
            IrcServerMessage lineMess;
	    motd = analisi.getMotd(); 
            // saluto
	    if (motd.length()==0)
		    motd = "Ciao io sono il kernel dei gatti";
	    System.out.println(motd);
	    while(true){
               try{
                 System.out.print(">> ");
                 line = in.readLine();
               } catch(IOException ex){
               	 System.out.println("[local:] - Impossibile leggere dal socket: " + ex.getMessage());
               	 System.exit(1);
               }
               prefix = visitor;
	       params = nickName + " :" + line.toLowerCase().trim();

	       trattaPrivMsg(prefix, params);
            }
        }

      /**
       * La documentazione di questa funzione e' la stessa della funzione omonima in @see IrcClient
       */
        public void trattaPrivMsg(String prefix, String params) {
            StringTokenizer tok = new StringTokenizer(params, " ");
	    String k = tok.nextToken();
	    String[] response;

	    int nRes = 1;
	    boolean command = false;

	    if (k.equals(nickName)) {
	    	String toNick = trovaNick(prefix);
	    	if (learningTable.containsKey(toNick))
	    	   if (((String)learningTable.get(toNick)).equals("begin_learn"))
	    	      learn2(toNick, params);
	    	   else
	    	      learnEnd(toNick, params);
	    	else {
	    	    String k2 = tok.nextToken();
	    	    if (k2.equals(":versione")){
	    		response = analisi.version();
	    		for (int i=0; i<response.length; i++)
	    		    System.out.println(response[i]);
	            }
	            else if (k2.trim().equals(":chi sei?")){
	        	response = analisi.chisei();
	        	for (int i=0; i<response.length; i++)
	    		    System.out.println(response[i]);
	            }
	            else if (k2.substring(1).trim().equals(nickName) || 
	                     k2.substring(1).trim().equals(nickName.toLowerCase())){
	                System.out.println("Si?");
	       	    }
	       	    else if (k2.equals(":stop")){
	       		response = new String[1];
	       		response[0] = "C'è tanto da imparare nella vita";
	       		System.out.println("Ordine di arresto programma. Ciao a presto");
	       		System.out.println("Mi fermo qui. Ciao a tutti");
	       		for (int i = 0; i < 20000; i++);
	       		System.out.println("QUIT");
	       		System.exit(0);
	       	    }
	            else {
	       	        response = analisi.generation(toNick, params, 1);
	       	        if (response != null){
	       	           if (response[0].indexOf("CMD") > -1){
	       	           	 nRes =  2;
	       	                 response = analisi.esegueComando(response[0], params, nRes);
	       	                 command = true;
	       	           }
	       	           if (!response[0].equals("learn")){
	       	              for (int i = 0; i < nRes; i++){
	       	                  System.out.println(response[i]);
	       	              }
	       	              if (command){
	       	              	nRes = 1;
	       	              	command = false;
	       	              }
	       	           }
	       	           else {
	       	     	       learningTable.put(toNick, "begin_learn");
	       	     	       System.out.println(toNick + " Non so cosa significhi. La parola chiave quale è?");
	                   }
	       	         }
	       	    }
	       }
	    }
	}

	/**
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
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
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
	 */
        private void learn2(String toNick, String params) {
           int k = params.indexOf(":");
           String newKeyWord   = params.substring(k+1).trim();
           if (newKeyWord.indexOf(nickName) >-1 ){ //okkio! Stanno tentando l'hack del nome!
	        System.out.println("No no non puoi darmi come chiave qualcosa con il mio nome...");
		System.out.println("poi va a finire che quando la gente mi invoca io rispondo cosi'");
		System.out.println("e mica ci casco... sce'!");
		learningTable.remove(toNick);
           }
	   else {
	      learningTable.put(toNick, newKeyWord);
              System.out.println("Mi dici una bella frase che riguarda ciò? Io la imparo.");
           }
	}
	   
	/**
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
	 */
        private void learnEnd(String toNick, String params){
            int k = params.indexOf(":");
            String newPhraseLearned = params.substring(k+1).trim();
            String newKeyWord   = (String)learningTable.get(toNick);
	    //Controllo bug di gi0
	    if (newKeyWord.equals("") || newPhraseLearned.equals("")){
		System.out.println("Ehi mica vorrai fregarmi con qualche stupido hack... sei un lamer :))");
	    }
	    else {
	    	System.out.println("Ho capito: " + newKeyWord + " " + newPhraseLearned);
	        System.out.println("Grazie davvero. Non si finisce mai di imparare :-)");
                analisi.learning(newKeyWord, newPhraseLearned);
	    }
            learningTable.remove(toNick);
        }
}
