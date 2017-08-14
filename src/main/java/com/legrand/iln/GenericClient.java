package com.legrand.iln;

import java.net.*;
import java.io.*;

/* $Id: GenericClient.java,v 1.4 2003/06/02 20:35:09 legrand Exp legrand $ */

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
 * Client generico. Tutti i client dovrebbero ereditare da
 * questa classe, che fornisce un modello di comportamento
 * per i client TCP/IP.
 * I client reali devono reimplementare il metodo <code>gestione</code>
 * ed eventualmente il metodo <code>autenticate</code>
 *
 * @author  Monsieur Legrand
 * @version 1.2 rev 1
 * @date    02 giugno 2003
 */
public class GenericClient {
	/** Nome dell'Host al quale connettersi */
	public String mHost;

	/** Numero della porta remota alla quale connettersi */
	public int    mPort;

	/** Nome del servizio che si fornisce */
	public String nomeServizio;

	/** Oggetto di classe LogServer, che incapsula tutte le operazioni di logging */
	public LogServer mLog;
	
	/** 
	 * Costruttore. 
	 * @param nomeServizio  Il nome da assegnare al servizio
	 * @param aHost         Nome o ip dell'host remoto
	 * @param aPort         Porta remota
	 */
	public GenericClient(String nomeServizio, String aHost, int aPort){
		mHost = aHost;
		mPort = aPort;
		mLog = new LogServer(nomeServizio);
	}
	
	/**
	 * Effettua la connessione all'Host remoto.
	 *
	 * @return un Socket aperto e connesso all'Host remoto.
	 */
	public Socket connect()
	throws UnknownHostException, SocketException, IOException
	{
		Socket sock = new Socket(mHost, mPort);
		//per ora setto un Timeout infinito, poi un giorno si vedra'.
		sock.setSoTimeout(0);
		
		return sock;
        }
	
	/**
	 * Effettua le operazioni di autenticazione sull'Host remoto. 
	 * Le classi che ereditano da GenericClient devono sovrascrivere
	 * questo metodo con le opportune operazioni di autenticazione.<br>
	 * Questa versione di base restituisce sempre True.
	 *
	 * @return True se l'autenticazione ha buon fine, false altrimenti.
	 */
	public boolean autenticate(Socket sock){ return true; }
	
	/**
	 * Interrompe la connessione
	 */
	public void disconnect(Socket sock) 
	throws IOException
	{
		sock.close();
        }
	
	/**
	 * Metodo che deve essere obbligatoriamente riscritto dalle classi
	 * figlie. Implementa la gestione del servizio.
	 */
	public void gestione(Socket sock){ }
	
	/**
	 * Logga una stringa
	 * @param aMess Stringa da loggare
	 */
	public void log(String aMess){
		mLog.logga(aMess);
	}
}
