package com.legrand.iln;

import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.methods.*;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.exceptions.*;
import org.telegram.telegrambots.updateshandlers.*;

public class TelegramIlnBot extends TelegramLongPollingBot {

	 @Override
	 public void onUpdateReceived(Update update) {
	        // We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
		            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
			                         .setChatId(update.getMessage().getChatId())
		                                 .setText(update.getMessage().getText());
		            try {
		                 sendMessage(message); // Call method to send the message
		            } catch (TelegramApiException e) {
		                 e.printStackTrace();
		            }
		}
    	 }
				
         @Override
         public String getBotUsername() {
	    return "Gattino";
         }
				
         @Override
         public String getBotToken() {
           return "405806016:AAF-QgVcjcijshxn2USVGrioV1y5lKjijng";
	 }
}
