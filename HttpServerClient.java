package com.legrand.iln;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;

public class HttpServerClient {

	private int port;
	private String host;
	private String nickName;
	private Analyzer analisi;
	private boolean debug = false;
	private LogServer log;

	private Hashtable learningTable = new Hashtable();

	/**
	 * nick debug port host
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String nick = args[0];
		int port = Integer.parseInt(args[2]);
		String host = args[3];
		int aDeb = Integer.parseInt(args[1]);
		boolean ldeb;
		if (aDeb == 1)
			ldeb = true;
		else
			ldeb = false;
		new HttpServerClient(port, host, nick, ldeb);
	}

	public HttpServerClient(int port, String host, String nickname, boolean debug) {
		this.port = port;
		this.host = host;
		this.nickName = nickname;
		this.debug = debug;
		this.analisi = new Analyzer(this.nickName, this.debug);
		this.log = new LogServer("iln");
		this.start();
	}

	private void start() {

		Undertow server = Undertow.builder()
				.addHttpListener(this.port, this.host, new BlockingHandler(new MyHttpHandler())).build();
		server.start();

	}

	private class MyHttpHandler implements HttpHandler {

		@Override
		public void handleRequest(HttpServerExchange exchange) throws Exception {
			String body = "";
			String prefix = "",  params;
			String messaggio = "";
			String[] response;

			Map<String, Deque<String>> getParams = exchange.getQueryParameters();
			if (!getParams.isEmpty()) {
				prefix = getParams.get("nick").getLast();
				messaggio = getParams.get("messaggio").getLast();
				messaggio = java.net.URLDecoder.decode(messaggio, "UTF-8");
			} else {
				//messaggio in post TBC
				BufferedReader reader = null;
				StringBuilder builder = new StringBuilder();

				try {
					reader = new BufferedReader(new InputStreamReader(exchange.getInputStream()));

					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				body = builder.toString();
			}

			
			
			params = nickName + " :" + messaggio.toLowerCase().trim();
			log.logga(prefix + " --> " + nickName + ": " + messaggio.toLowerCase().trim());

			response = trattaPrivMsg(prefix, params);

			for (int i = 0; i < response.length; i++) {
				log.logga(nickName + " --> " + prefix + ": " + response[i]);
				body+=response[i];
				if(i<response.length-1) {
					body+=" ";
				}
			}
			
			
			exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
			exchange.getResponseSender().send(body);

		}

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
						// log.logga("Entrato nell'else del learn");
						learningTable.put(toNick, "begin_learn");
						response[0] = toNick + " Non so cosa significhi. La parola chiave quale è?";
						// log.logga("Settato " + response[0]);
						return response;
					}
				}
			}
		}
		return response;
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
