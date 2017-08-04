package com.legrand.iln;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.*;

public class Main {
	public static void main(String[] args) {
          // Initialize Api Context
          ApiContextInitializer.init();

          // Instantiate Telegram Bots API
          TelegramBotsApi botsApi = new TelegramBotsApi();

          // Register our bot
          try {
          	botsApi.registerBot(new TelegramIlnBot());
	  } catch (Exception e) {
	             e.printStackTrace();
	  }
	}
}
