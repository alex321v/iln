package com.legrand.iln;

import java.io.*;
import java.util.*;


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
 * Classe che implementa varie funzioni di utilita' 
 * per gli ILN.
 *
 * @author Monsieur Legrand
 * @version 1.45rev 0
 * @date   05 agosto 2017
 */

public class RobotUtils {

  /**
   * Legge i dati da un file di testo. La lettura avviene per righe.
   *
   * @param fileName stringa contenente il nome del file da leggere.
   *                 l'estensione .txt viene aggiunta automaticamente.
   *
   * @return una Collection contenente tutte le stringhe lette dal file.
   */
  public static Collection leggeDati(String fileName) {
	Collection risposta = new ArrayList();
	String lettura;
	FileInputStream f = null;
	InputStreamReader inp = null;
	BufferedReader fIn = null;

        try{
	    f = new FileInputStream(fileName + ".txt");
	    inp = new InputStreamReader(f);
	    fIn = new BufferedReader(inp);
	}catch (IOException ex){
            System.out.println("Errore nell'apertura del file:");
	    ex.printStackTrace();
	    System.exit(1);
	}

        try{
	    while ( (lettura = fIn.readLine()) != null )
		risposta.add(lettura);
         }catch(IOException ex){
		System.out.println("Errore durante la lettura dal file:");
	        ex.printStackTrace();
		System.exit(1);
	 }

         try{
             fIn.close();
	     inp.close();
	     f.close();
	 }catch(IOException ex){
		System.out.println("Errore durante la chiusura del file:");
		ex.printStackTrace();
		System.exit(1);
	 }

	 return risposta;
    }


   /**
    * Aggiunge un nuovo nome all'elenco dei nomi conosciuti dal programma.<br>
    * Per nomi si intendono i nickname che gia' hanno interagito con il programma.
    *
    * @param arg una stringa contenente il nome del file contenente i nomi noti.
    *            L'estensione .txt viene aggiunta automaticamente.
    * @param nick il nome/nickname da aggiungere.
    */
    public static void aggiungiNome(String arg, String nick) {
    	FileWriter outStream;
    	PrintWriter out;
    	try{
            outStream = new FileWriter(arg + ".txt", true);
            out = new PrintWriter(outStream, true);
            out.println(nick);
            out.close();
	    outStream.close();
	} catch(IOException ec){
	    System.out.println("Errore nell'accesso al file di nomi: " + arg + ".txt");
	    ec.printStackTrace();
	}
    }

}
