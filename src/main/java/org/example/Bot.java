package org.example;

import handlers.CurrencyHandler;
import handlers.UpdateHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import profiles.UsersData;

import java.io.IOException;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

    private final UpdateHandler updateHandler;
    private final CurrencyHandler currencyHandler;
    private final UsersData usersData;

    public Bot() {
        currencyHandler = new CurrencyHandler(this);
        usersData = new UsersData();
        updateHandler = new UpdateHandler(this, currencyHandler, usersData);
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                updateHandler.handlerText(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (update.hasCallbackQuery()) {
            try {
                currencyHandler.handleCallback(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "@VEJAVA_bot";
    }

    public String getBotToken() {
        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty("token");
    }
}