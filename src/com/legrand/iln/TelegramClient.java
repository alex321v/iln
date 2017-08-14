package com.legrand.iln;

import java.io.*;
import java.util.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
 * Classe che implementa il client specifico per la connessione ad un servizio
 * IRC.
 *
 * @author Monsieur Legrand
 * @version 1.5 rev 1
 * @date 04 agosto 2017
 */

public class TelegramClient {
	String nickName;
	boolean debug = false;
	String motd = new String();
	Analyzer analisi;
	String tipoResponse;
	String visitor;
	BufferedReader in;
	LogServer log;

	Hashtable learningTable = new Hashtable();

	/**
	 * Fa partire il client.<BR>
	 * Uso: <code>java TestClient nick debugLevel</code><br>
	 */
	public static void main(String[] args) {
		boolean ldeb;
		if (args.length < 2)
			misuse();
		String nick = args[0];
		int aDeb = Integer.parseInt(args[1]);
		if (aDeb == 1)
			ldeb = true;
		else
			ldeb = false;

		// Initialize Api Context
		ApiContextInitializer.init();

		// Instantiate Telegram Bots API
		TelegramBotsApi botsApi = new TelegramBotsApi();

		// Register our bot
		try {
			botsApi.registerBot(new TelegramIlnBot(nick, ldeb, new LogServer("iln")));
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Funzione di utilita' invocata se il programma viene fatto partire con un
	 * numero insufficiente di argomenti.
	 */
	private static void misuse() {
		System.out.println("[local:] - Uso: java TestClient <nick> <debug> ");
		System.out.println("[local:] -       debug = 1 - più messaggi in log. debug = 0 - meno messaggi");
		System.exit(0);
	}

}
