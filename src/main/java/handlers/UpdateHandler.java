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

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    private String getTextFromUpdate(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()){
            return update.getMessage().getText();
        }
        return "";
    }

    public void handlerText(Update update) throws TelegramApiException{
        Long chatID = getChatIdFromUpdate(update);
        String Objtext = handleText(getTextFromUpdate(update));
        if ((Objtext) == "1") {
            currencyHandler.currencyResponse(chatID);
        }
        else if (isNumeric(Objtext)){
            currencyHandler.convertion(chatID, Objtext);
        }
        else {
            sendMessage(chatID, Objtext);
        }
    }


    public static String handleText(String message){
            switch (message) {
                    case "/help": return "Я умею конвертировать основные для России валюты. Напиши /set_currency";
                    case "/start": return "Вас приветствует финансовый бот. Я умею: конвертировать основные для России валюты Напиши /set_currency для использования";
                    case "/set_currency": return "1";
            }
        return message;
    }

}
