package com.legrand.iln;

import java.util.*;

/* $Id: IrcServerMessage.java,v 1.4 2004/01/14 16:23:30 legrand Exp legrand $ */

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
 * Classe che incapsula un messaggio di protocollo IRC.
 *
 * @author Monsieur Legrand
 * @version 1.4 rev 1
 * @date  29 dicembre 2003
 */
public class IrcServerMessage {

        private String lMessage;
        private String prefix;
        private String command;
        private String parameters = "";
        private String treDigit;
       
        /**
	 * Costruttore di default. Vuoto.
	 */
	public IrcServerMessage() { }
        
	/**
	 * Costruttore. Istanzia un IrcServerMessage a partire da una
	 * stringa proveniente dal server.
	 * @param aMess Il messaggio del IRCServer da incapsulare.
	 */
        public IrcServerMessage(String aMess){
        	setMessage(aMess);
        }

        /**
	 * Restituisce l'intero messaggio.
	 */
	public String getMessage()    {return lMessage;}
 
	/**
	 * Restituisce il prefisso del messaggio.
	 */
	public String getPrefix()     {return prefix;}
 
	/**
	 * Restituisce il comando di protocollo trasmesso.
	 */
	public String getCommand()    {return command;}

	/**
	 * Restituisce i parametri del messaggio, che di solito
	 * costituiscono il testo trasmesso vero e proprio.
	 */
	public String getParameters() {return parameters;}
 
	/**
	 * Restituisce il codice numerico del comando di protocollo.
	 */
	public String getTreDigit()   {return treDigit;}
 
	/**
	 * Setta un messaggio IRC grezzo proveniente da network
	 * all'interno di un oggetto della classe.
	 */
        public void setMessage(String aMess) {
        	lMessage = aMess;
        	int counter = 0;
        	StringTokenizer tokenizer = new StringTokenizer(aMess, " ");
        	while (tokenizer.hasMoreTokens()) {
        		String stringhetta = tokenizer.nextToken();
        		if (counter == 0){ 
        		  if (stringhetta.charAt(0) == ':')
        		        prefix = stringhetta;
        		  else {
        		  	prefix = null;
        		  	command =  stringhetta;
        		  }
        		  counter++;
        		}
        		else if (counter == 1) {
        			if (prefix == null)
        			     parameters += stringhetta + " ";
        			else command = stringhetta;
        			counter++;
        	             }
        		     else {
        		     	   parameters += stringhetta + " ";
        		     	   counter++;
        		     }
                }
                treDigit = command;
        }
        
}
