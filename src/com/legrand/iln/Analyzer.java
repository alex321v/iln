package com.legrand.iln;

import java.io.*;
import java.util.*;
import java.net.*;

/* $Id: Analyzer.java,v 1.4.2.1 2004/01/14 16:18:47 legrand Exp legrand $ */

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
 * Classe Analyzer
 * Modello di comportamento di un analizzatore generico.<BR>
 * Tutti gli analizzatori devono per forza ereditare da questa 
 * classe.<BR>
 * Il Client IRC dichiarera' sempre un puntatore a superclasse,
 * cioe' un puntatore ad Analyzer, poi grazie al polimorfismo 
 * sara' istanziato un analizzatore reale.<br>
 * In alternativa si puo' usare questo analizzatore generico.<p>
 *
 * Tutti i metodi di questo analizzatore sono gusci di metodi
 * dell'appropriato oggetto di classe Animatore (o sua
 * sottoclasse).<br>
 * 
 *
 * @author  Monsieur Legrand
 * @version 1.4 rev 2
 * @date    14 gennaio 2004
 */
public class Analyzer
{

    /**
     * Puntatore alla classe Animatore, che contraddistingue la
     * personalità dell'automa.
     */
    Animatore generResponse;

    /**
     * Costruttore.<br>
     * A seconda del tipo di personalita' richiesta verra' 
     * istanziato un diverso Animatore.
     *
     * @param nick  nickname dell'automa.
     * @param tipo  Personalita'.
     * @param debug Fissa il livello di debugging.
     */
    public Analyzer(String nick, boolean debug) {
	generResponse = new Risposte(nick, debug);
    }

    /**
     * Comando di controllo di versione.
     * 
     * @return Array di stringhe contenente l'etichetta della versione.
     */
    public String[] version(){ 
       return generResponse.version();
    }

    /**
     * Restituisce il messaggio di inizio del programma.
     *
     * @return Una stringa contenente il messaggio
     */
    public String getMotd() {
	    return generResponse.getMotd();
    }

    /** 
     * Comando di identificazione.
     *
     * @return Array di stringhe contenente l'identificazione.
     */
    public String[] chisei() {
    	return generResponse.chisono();
    }
    
    /**
     * Metodo che implementa il cervello dell'automa.<BR>
     * E' in grado di generare la risposta ad uno stimolo proveniente
     * da una query privata.
     *
     * @param nick      Nickname dello stimolatore
     * @param keyPhrase Stimolo
     * @param howMuch   Numero massimo di righe della reazione
     *
     * @return howMuch stringhe contenenti la risposta allo stimolo.
     */
    public String[] generation(String nick, String keyPhrase, int howMuch){
	return generResponse.generation(nick, keyPhrase, howMuch);
    }

    /**
     * Metodo che implementa il cervello pubblico dell'automa.<br>
     * E' in grado di generare la risposta ad uno stimolo proveniente
     * dal canale IRC.
     *
     * @param keyPhrase Stimolo
     * @param howMuch Numero massimo di righe della reazione.
     *
     * @return howMuch righe contenenti la risposta allo stimolo.
     */
    public String[] generation(String keyPhrase, int howMuch){
	return generResponse.generation(keyPhrase, howMuch);
    }
   
    /**
     * Esegue comando di grammatica.
     *
     * @param comando   Il comando da eseguire
     * @param keyPhrase Lo stimolo.
     * @param howMuch   Numero massimo di righe della reazione
     *
     * @return howMuch righe contenenti la risposta allo stimolo.
     */
    public String[] esegueComando(String comando, String keyPhrase, int howMuch) { 
    	return generResponse.esegueComando(comando, keyPhrase, howMuch);
    }
    
    /**
     * Gestore del learning dell'automa. Questo metodo viene invocato quando termina
     * un singolo learning e c'è da inserire nella base di conoscenza una nuova
     * coppia chiave-valore.
     * 
     * @param newKey    chiave da inserire.
     * @param newPhrase Valore da inserire.
     */
    public void learning(String newKey, String newPhrase) {
    	generResponse.learning(newKey, newPhrase);
    }

}
