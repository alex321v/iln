package com.legrand.iln;

import java.io.*;
import java.util.*;

/* $Id: LogServer.java,v 1.4 2003/06/02 20:41:09 legrand Exp legrand $ */

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
 * Classe che permette a qualunque server di rete che estende
 * <code>GenericServer</code> di loggare messaggi su file system
 * locale. Serve per motivi di controllo e di sicurezza.
 *
 * @author Monsieur Legrand
 * @version 1.4 rev 0
 * @date 02 giugno 2003
 */
public class LogServer
{
	private String nomeServizio;
	private PrintWriter out;
	private FileWriter outStream;

        /**
	 * Costruttore. Istanzia un nuovo log associato ad un file su File System.
	 *
	 * @param name  Il nome del file di log. Ad esso verra' aggiunta automaticamente l'estensione
	 * .log
	 */
	public LogServer(String name){
		nomeServizio = name;
		Date today = new Date();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(today);

                String timeStart = setStringona(cal);
		try{
		    outStream = new FileWriter(nomeServizio + ".log", true);
		    out = new PrintWriter(outStream, true);
		} catch(IOException ec){
			System.out.println("Errore nell'accesso al file di log: " + nomeServizio + ".log");
			ec.printStackTrace();
		}

		out.println("");
		out.println("****** Inizio logging " + nomeServizio + " " + timeStart + " ******");
		
	}

    /**
     * Scrive su file di log un messaggio.<BR>
     * E' responsabilita' del chiamante il montaggio della stringa messaggio.<BR>
     * Questo metodo provvedera' solo ad agganciare davanti al messaggio il nome
     * del servizio ed il TimeStamp.
     * 
     * @param req  Il messaggio da loggare.
     */
	public void logga(String req){
		Date today = new Date();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(today);

                String timeNow = setStringhina(cal);

		System.out.println(timeNow + " " + req);
		out.println(timeNow + " " + req);
	}

    /**
     * Chiude il file di log.
     */
    public void close(){
		try{
		    out.close();
		    outStream.close();
		}catch(IOException ex){
			System.out.println("Errore durante la chiusura del file di log");
			ex.printStackTrace();
		}

	}

	private String setStringona(GregorianCalendar cal){
		String am_pm[] = {"am", "pm"};
		String risposta = "" + cal.get(Calendar.DATE);
		       risposta += "/" + (cal.get(Calendar.MONTH) + 1);
		       risposta += "/" + cal.get(Calendar.YEAR);
		       if (am_pm[cal.get(Calendar.AM_PM)].equals("am"))
				risposta += " " + cal.get(Calendar.HOUR);
		       else risposta += " " + (cal.get(Calendar.HOUR) + 12);
		       if (cal.get(Calendar.MINUTE) < 10)
				risposta += ":0" + cal.get(Calendar.MINUTE);
		       else
		                risposta += ":" + cal.get(Calendar.MINUTE);
		       if (cal.get(Calendar.SECOND) < 10)
				risposta += ":0" + cal.get(Calendar.SECOND);
		       else
			        risposta += ":" + cal.get(Calendar.SECOND);

		return risposta;
	}

	private String setStringhina(GregorianCalendar cal){
		String am_pm[] = {"am", "pm"};
		String risposta;
		if (am_pm[cal.get(Calendar.AM_PM)].equals("am"))
			risposta = "" + cal.get(Calendar.HOUR);
		else risposta = "" + (cal.get(Calendar.HOUR) + 12);
		if (cal.get(Calendar.MINUTE) < 10)
			risposta += ":0" + cal.get(Calendar.MINUTE);
		else    risposta += ":" + cal.get(Calendar.MINUTE);
		if (cal.get(Calendar.SECOND) < 10)
			risposta += ":0" + cal.get(Calendar.SECOND);
		else    risposta += ":" + cal.get(Calendar.SECOND);

		return risposta;
	}
}
