package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class App
{
    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot();
        TelegramBotsApi botapi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botapi.registerBot(bot);
            System.out.println("Bot is ready");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}



