package com.legrand.iln;

import java.io.*;
import java.util.*;
import java.net.*;

/* $Id: Iastemmiatore.java,v 1.4.2.1 2004/01/14 16:20:02 legrand Exp legrand $ */

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
 * Modulo di generazione automatica di bestemmie.<br>
 * Il database delle bestemmie e' prelevato dal Iastemmia server:
 * <a href="http://www.inventati.org/internattiva/iast/">Iastemmia Server - 
 * http://www.inventati.org/internattiva/iast</a>. Il Iastemmia Server e' un
 * free porting in php con interfaccia web del "prcd".
 *
 * @author  Monsieur Legrand
 * @version 1.4 rev 3
 * @date    02 giugno 2003
 */
public class Iastemmiatore implements Animatore
{
	/** Numero massimo di bestemmie generabili */
	private int maxIast = 15;

        /** Array di stringhe che conterra' le bestemmie caricate da file */
	private	String[] vettLette;
	
	/** Oggetto necessario per l'estrazione di numeri casuali */
	private Random random;
	private int counter;

	/**
	 * Costruttore. Istanzia un nuovo Iastemmiatore caricando le
	 * bestemmie da file.
	 */
	public Iastemmiatore(){
		Collection iastemmie;
		//Invoca l'utility che legge da un file.
		iastemmie = RobotUtils.leggeDati("IastemmiaFile");
		int i = 0;
		Iterator it = iastemmie.iterator();
		vettLette = new String[iastemmie.size()];
		while (it.hasNext()) {
			String tr = (String)it.next();
			vettLette[i] = tr;
			i++;
	        }
	}

	/**
	 * Metodo di utility che restituisce una descrizione della versione della classe.
	 *
	 * @return Un vettore di Stringhe contenente la descrizione.
	 */
	public String[] version(){
		String[] risposta = {
		        "************* Begin Info ****************", 
		        "IastemmiaServer versione 1.4.3 stabile. ",
		        "Compilata il 26 marzo 2002",
			"Home page: http://www.inventati.org/internattiva/iast/",
		        "A cura dei P.I.N.Q.D - Programmatori Incazzati Napoletani Quasi Disoccupati",
		        "Server in puro Java al 100% alla faccia di Micro$oft e all'anima di Bill Gates.",
		        "Soprattutto a li mortacci di chi ci licenzia e ci costringe ad emigrare!",
		        "Questo è un server pubblico di Bestemmie.",
		        "Il protocollo è disponibile per chiunque voglia sviluppare applicazioni client",
		        "che mettono a disposizione il servizio di bestemmie via Internet",
		        "o proporre nuovi servizi.",
		        "************* End Info ******************" };
                return risposta;
	}

	/**
	 * Restituisce una autodescrizione.
	 *
	 * @return Un vettore di Stringhe contenente la descrizione
	 */
	public String[] chisono() {
		String[] risposta = {"Sono il bestemmiatore on line :-)",
		                     "Non ti piaccio?"};
		
		return risposta;
	}

	/**
	 * Restituisce il motd, messaggio di ingresso.
	 *
	 * @return Una stringa contenente il messaggio di benvenuto
	 */
	public String getMotd() { return new String("porcodio!"); }
	
	/**
	 * Metodo la cui presenza e' forzata dall'interfaccia Animatore.
	 * Viene invocato quando si invia uno stimolo da una query.
	 * <br>
	 * @param nick      Il nickname di chi e' in query.
	 * @param keyPhrase La frase stimolo.
	 * @param howMuch   Numero di reazioni da generare.
	 */
	public String[] generation(String nick, String keyPhrase, int howMuch){
	        return generation(keyPhrase, howMuch);
        }

	/**
	 * Metodo la cui presenza e' forzata dall'interfaccia Animatore.
	 * Viene invocato quando si invia uno stimolo dal canale.
	 * <br>
	 * Estrae delle bestemmie casuali dal vettore @see vettLette
	 * @param keyPhrase La frase stimolo.
	 * @param howMuch   Numero di reazioni da generare.
	 */
	public String[] generation(String keyPhrase, int howMuch){ 
		random = new Random();
		//l'array di stringhe risposta conterra' le bestemmie estratte
		//da restituire al chiamante
		String[] risposta = new String[howMuch];
		Vector estratti = new Vector();
		int k;
		int i = 0;
		while ( i < howMuch) {
			k = selRandom();
			//ci assicuriamo di non estrarre piu' bestemmie identiche
			if ( !(estratti.contains(new Integer(k))) )
			{
				estratti.add(new Integer(k));
				i++;
			}
		}
		for (int j=0; j < howMuch; j++)
		     risposta[j] = vettLette[((Integer)estratti.get(j)).intValue()];

	        return risposta;
	}

	/**
	 * Metodo la cui presenza e' forzata dall'interfaccia @see Animatore.
	 * Non e' previsto il learning on-line del bestemmiatore, per cui
	 * questo metodo e' vuoto.
	 *
	 */
	public void learning(String newKey, String newPhrase) { }

	/**
	 * Metodo la cui presenza è forzata dall'interfaccia @see Animatore<br>
	 * Per ora non e' prevista una grammatica di comandi, per cui questo metodo
	 * restituisce un reference ad un array di stringhe nulle.
	 */
	public String[] esegueComando(String comando, String keyPhrase, int howMuch) { 
		String[] risposta = new String[maxIast];

		return risposta;
		}

        private int selRandom() {
		int r = random.nextInt();
		if (r<0) r = -r;
		return r % (vettLette.length);
	}
	


}
