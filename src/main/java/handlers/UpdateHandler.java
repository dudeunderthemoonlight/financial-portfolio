package handlers;

import org.example.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UpdateHandler {
    public Bot bot;
    public CurrencyHandler currencyHandler;

    public UpdateHandler(Bot bot, CurrencyHandler currencyHandler) {
        this.bot = bot;
        this.currencyHandler = currencyHandler;
    }

    public void sendMessage(Long chatID, String text) throws TelegramApiException {
        bot.execute(SendMessage.builder()
                .chatId(chatID.toString())
                .text(text)
                .build());
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    private String getTextFromUpdate(Update update) {
        return update.getMessage().getText();
    }

    private void helper(Long chatID) throws TelegramApiException {
        sendMessage(chatID,"Я умею конвертировать основные для России валюты. Напиши /set_currency");
    }

    private void starter(Long chatID) throws TelegramApiException {
        sendMessage(chatID,"Вас приветствует финансовый бот." +
                    "Я умею: конвертировать основные для России валюты" +
                    "Напиши /set_currency для использования");
    }


    public void handleText(Update update) throws TelegramApiException {
        Message message = update.getMessage();
        Long chatID = getChatIdFromUpdate(update);
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                    case "/help": helper(chatID);
                        break;
                    case "/start": starter(chatID);
                        break;
                    case "/set_currency":
                        currencyHandler.currencyResponse(chatID);
                    default: currencyHandler.convertion(message);
            }
        }
    }

}
