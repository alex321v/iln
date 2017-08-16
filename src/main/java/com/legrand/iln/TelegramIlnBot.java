package com.legrand.iln;

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
 * Classe che implementa il client specifico per la connessione
 * a Telegram.
 *
 * @author Monsieur Legrand
 * @version 1.5 rev 1
 * @date   04 agosto 2017
 */
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Properties;
import java.io.*;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

public class TelegramIlnBot extends TelegramLongPollingBot {

	String nickName;
	boolean debug = false;
	String motd = new String();
	Analyzer analisi;
	String tipoResponse;
	String visitor, nolearn;
	LogServer log;

	Chat mchat;
	
	public static String token;

	Hashtable learningTable = new Hashtable();

	/**
	 * Costruttore. Viene invocato automaticamente alla partenza del programma. Non
	 * c'e' bisogno di effettuare chiamate esplicite.
	 *
	 * @param aNick
	 *            nickname dell'istanza del programma
	 * @param deb
	 *            un booleano che indica il livello di debug: true = debug alto,
	 *            false = debug basso.
	 */
	public TelegramIlnBot(String aNick, boolean deb, LogServer alog) {
		nickName = aNick;
		debug = deb;
		log = alog;
		analisi = new Analyzer(nickName, debug);
		
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);
			nolearn = prop.getProperty("nolearn");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		String line = null;
		String prefix, comando, params;
		String[] response;

		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			// Set variables
			long chat_id = update.getMessage().getChatId();
			mchat = update.getMessage().getChat();
			prefix = update.getMessage().getFrom().getUserName();

			if (prefix == null) {
				prefix = update.getMessage().getFrom().getFirstName();
			}

			params = nickName + " :" + update.getMessage().getText().toLowerCase().trim();
			log.logga(prefix + " --> " + nickName + ": " + update.getMessage().getText().toLowerCase().trim());

			response = trattaPrivMsg(prefix, params);

			for (int i = 0; i < response.length; i++) {
				log.logga(nickName + " --> " + prefix + ": " + response[i]);
				SendMessage message = new SendMessage() // Create a message object object
						.setChatId(chat_id).setText(EmojiParser.parseToUnicode(response[i]));
				try {
					sendMessage(message); // Sending our message object to user
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}	
		}
	}

	@Override
	public String getBotUsername() {
		return nickName;
	}

	@Override
	public String getBotToken() {
		//return "405806016:AAF-QgVcjcijshxn2USVGrioV1y5lKjijng";
		return TelegramIlnBot.token;
	}

	/**
	 * La documentazione di questa funzione e' la stessa della funzione omonima
	 * in @see IrcClient
	 */
	public String[] trattaPrivMsg(String prefix, String params) {
		StringTokenizer tok = new StringTokenizer(params, " ");
		String k = tok.nextToken();
		String[] response = new String[3];

		int nRes = 1;
		boolean command = false;

		if (k.equals(nickName)) {
			String toNick = prefix;
			if (learningTable.containsKey(toNick))
				if (((String) learningTable.get(toNick)).equals("begin_learn"))
					response = learn2(toNick, params);
				else
					response = learnEnd(toNick, params);
			else {
				String k2 = tok.nextToken();
				if (k2.equals(":versione")) {
					response = analisi.version();
					return response;
				} else if (k2.trim().equals(":chi sei?")) {
					response = analisi.chisei();
					return response;
				} else if (k2.substring(1).trim().equals(nickName)
						|| k2.substring(1).trim().equals(nickName.toLowerCase())) {
					response[0] = "Sì?";
					response[1] = "Che vuoi?";
					response[2] = "Non perdere tempo, parla!";
					return response;
				} else if (k2.equals(":stop")) {
					System.out.println("Ordine di arresto programma. Ciao a presto");
					System.out.println("Mi fermo qui. Ciao a tutti");
					System.out.println("QUIT");
					System.exit(0);
				} else {
					response = analisi.generation(toNick, params, 1);
					if (response != null && response[0] != "learn") {
						if (response[0].indexOf("CMD") > -1) {
							// log.logga("command = true");
							nRes = 2;
							response = analisi.esegueComando(response[0], params, nRes);
							command = true;
						}
						if (!response[0].equals("learn")) {
							return response;
						}
						if (command) {
							nRes = 1;
							command = false;
							// log.logga("command = false");
						}
					} else {
						 if(mchat.isGroupChat()){
							response[0] = Configurations.NOLEARN;
							return response;
						}
						else {
							learningTable.put(toNick, "begin_learn");
							response[0] = toNick + " Non so cosa significhi. La parola chiave quale è?";
							return response;
						}
					}
				}
			}
		}
		return response;
	}

	/**
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
	 */
	private String trovaNick(String stringa) {
		StringBuffer rispostina = new StringBuffer();
		int i = 1;
		while (stringa.charAt(i) != '!') {
			rispostina.append(stringa.charAt(i));
			i++;
		}

		return rispostina.toString();

	}

	/**
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
	 */
	private String[] learn2(String toNick, String params) {
		String[] response = new String[3];
		int k = params.indexOf(":");
		String newKeyWord = params.substring(k + 1).trim();
		// log.logga ("Siamo in leanr2");
		if (newKeyWord.indexOf(nickName) > -1) { // okkio! Stanno tentando l'hack del nome!
			response[0] = "No no non puoi darmi come chiave qualcosa con il mio nome...";
			response[1] = "poi va a finire che quando la gente mi invoca io rispondo così";
			response[2] = "e mica ci casco... sce'!";
			learningTable.remove(toNick);
		} else {
			// log.logga("Siamo nell'else di learn2");
			learningTable.put(toNick, newKeyWord);
			response[0] = "Mi dici una bella frase che riguarda ciò? Io la imparo.";
			response[1] = "Dai, una cosa simpatica...";
			response[2] = "Fammi migliorare...";
		}
		// log.logga("response[0] dopo l'else: " + response[0]);
		return response;
	}

	/**
	 * La documentazione di questa funzione e' la stessa di quella di @see IrcClient
	 */
	private String[] learnEnd(String toNick, String params) {
		String[] response = new String[2];

		int k = params.indexOf(":");
		String newPhraseLearned = params.substring(k + 1).trim();
		String newKeyWord = (String) learningTable.get(toNick);
		// Controllo bug di gi0
		if (newKeyWord.equals("") || newPhraseLearned.equals("")) {
			response[0] = "Ehi mica vorrai fregarmi con qualche stupido hack... sei un lamer :))";
			response[1] = "Non provarci più!";
		} else {
			response[0] = "Ho capito: " + newKeyWord + " " + newPhraseLearned;
			response[1] = "Grazie davvero. Non si finisce mai di imparare :-)";
			analisi.learning(newKeyWord, newPhraseLearned);
		}
		learningTable.remove(toNick);
		return response;
	}
}
