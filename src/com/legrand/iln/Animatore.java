package com.legrand.iln;

import java.io.*;
import java.util.*;
import java.net.*;

/* $Id: Animatore.java,v 1.4.2.1 2004/01/14 16:19:20 legrand Exp legrand $ */

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
 * Interfaccia generica di ogni Animatore.<br>
 * Descrive il modello di comportamento di ogni possibile animatore.<br>
 * Ciascuna implementazione dell'animatore deve obbligatoriamente
 * implementare l'interfaccia Animatore.
 * @author Monsieur Legrand
 * @version 1.4 rev 2 
 * @date  14 gennaio 2004 
 */
 
public interface Animatore
{
	/**
	 * Restituisce una o piu' stringhe che descrivono la versione
	 * dell'animatore.
	 * @return Le stringhe descrittive della versione.
	 */
	public String[] version();

	/**
	 * Restituisce il motd.<br>
	 *
	 * @return una singola stringa contenente il motd.
	 */
	public String getMotd();

	/**
	 * Genera una risposta ad uno stimolo proveniente da una query privata.<br>
	 * @param nick      Nickname del mittente dello stimolo.
	 * @param keyPhrase Lo stimolo.
	 * @param howMuch   Dimensione (in righe di testo) della reazione.
	 *
	 * @return Un'array di howMuch stringhe contenenti la reazione allo stimolo.
	 */
	public String[] generation(String nick, String keyPhrase, int howMuch);

	/**
	 * Genera una risposta ad uno stimolo proveniente dal canale.<br>
	 * @param keyPhrase Lo stimolo.
	 * @param howMuch   Dimensione (in righe di testo) della reazione.
	 *
	 * @return Un'array di howMuch stringhe contenenti la reazione allo stimolo.
	 */
	public String[] generation(String keyPhrase, int howMuch);

	/**
	 * Esegue un comando di grammatica corrispondente ad uno stimolo.<BR>
	 * @param comando   Il comando da eseguire.
	 * @param keyPhrase Lo stimolo.
	 * @param howMuch   Dimensione (in righe di testo) della reazione.
	 *
	 * @return Un'array di howMuch stringhe contenenti la reazione allo stimolo.
	 */
	public String[] esegueComando(String comando, String keyPhrase, int howMuch);

	/**
	 * Genera una descrizione identificativa dell'animatore.<br>
	 *
	 * @return Un array di stringhe contenente la descrizione dell'animatore.
	 */
	public String[] chisono();

	/**
	 * Inserimento nella base di conoscenza di una nuova sequenza di learning.<br>
	 * @param newKey    La nuova keyword.
	 * @param newPhrase La nuova reazione alla keyword.
	 */
	public void learning(String newKey, String newPhrase);

}
