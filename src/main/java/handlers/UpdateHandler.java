package handlers;

import org.example.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import profiles.UsersData;


public class UpdateHandler {
    public Bot bot;
    public CurrencyHandler currencyHandler;
    public UsersData usersData;
    public PortfolioHandler portfolioHandler;

    public UpdateHandler(Bot bot, CurrencyHandler currencyHandler, UsersData usersData) {
        this.bot = bot;
        this.currencyHandler = currencyHandler;
        this.usersData = usersData;
        this.portfolioHandler = new PortfolioHandler(usersData);
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
        if (!usersData.inData(chatID)) {
            usersData.addUser(chatID);
        }
        String objtext = handleText(getTextFromUpdate(update));
        if (objtext == "1") {
            currencyHandler.currencyResponse(chatID);
        }
        else if (objtext == "2") {
            sendMessage(chatID, portfolioHandler.responseEdit(chatID));
        }
        else if (objtext == "3"){
            sendMessage(chatID, portfolioHandler.getValue(chatID));
            sendMessage(chatID, portfolioHandler.givePortfolio(chatID));
        }
        else if (isNumeric(objtext)){
            currencyHandler.convertion(chatID, objtext);
        }
        else if (portfolioHandler.isEditEnable(chatID)){
            portfolioHandler.parseEdit(chatID, objtext);
            String string = portfolioHandler.givePortfolio(chatID);
            sendMessage(chatID, string);
        }
        else {
            sendMessage(chatID, objtext);
        }
    }

    public static String handleText(String message){
            switch (message) {
                    case "/help":
                        return  "Доступные команды:\n" +
                                "/set_currency - конвертация валюты, выберете оригинальную и целевую валюты " +
                                "и отправьте мне число (сумму оригинальной валюты)\n" +
                                "/edit - редактирование инвестиционного портфеля\n" +
                                "/fortfoliovalue - узнать текущую ценность своего портфеля в USD\n";
                    case "/start":
                        return  "Вас приветствует финансовый бот.\n" +
                                "Я умею:\n" +
                                "-- конвертировать основные для России валюты: /set_currency\n" +
                                "-- хранить и оценивать ваш инвестиционный портфель:\n" +
                                "   /edit для редактирования\n" +
                                "   /portfoliovalue узнать текущую ценность портфеля в USD";
                    case "/set_currency": return "1";
                    case "/edit": return "2";
                    case "/portfoliovalue": return "3";
            }
        return message;
    }
}
