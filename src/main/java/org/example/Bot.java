package org.example;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Bot extends TelegramLongPollingBot{

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.hasText()){
                try {
                    execute(
                            SendMessage.builder()
                                    .chatId(message.getChatId().toString())
                                    .text("You sent: \n" + message.getText())
                                    .build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {
        super.clearWebhook();
    }

    @Override
    public String getBotUsername() {
        return "@VEJAVA_bot";
        //возвращаем юзера
    }

    @Override
    public String getBotToken() {
        return "2093357793:AAEwdlZ0pTJ4L5D0fFYYTOefcf8-IZJxXCM";
        //Токен бота
    }


}