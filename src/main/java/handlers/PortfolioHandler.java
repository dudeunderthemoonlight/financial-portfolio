package handlers;

import currency.Currency;
import currency.CurrencyConversion;
import profiles.UsersData;
import currency.CurrencyMode;
import java.util.HashMap;
import java.util.Map;


public class PortfolioHandler {
    private final CurrencyMode currencyMode = CurrencyMode.getInstance();
    private final CurrencyConversion currencyConversion =
            CurrencyConversion.getInstance();

    public UsersData usersData;

    public PortfolioHandler(UsersData usersData){
        this.usersData = usersData;
    }

    public final Map<Long, Boolean> editMode = new HashMap<>();

    public Boolean isEditEnable(long chatID){
        return editMode.getOrDefault(chatID, false);
    }

    private void turnOnEditMode(long chatID) {
        editMode.put(chatID, true);
    }

    private void turnOffEditMode(long chatID) {
        editMode.put(chatID, false);
    }

    public String responseEdit(Long chatID){
        turnOnEditMode(chatID);
        return ("Вы можете изменить свой инвестиционный портфель.\n" +
                "Для внесения или удаления актива введите название актива, действие и сумму, " +
                "например: RUB +100");
    }

    public void parseEdit(Long chatID, String objtext){
        String[] subStr;
        subStr = objtext.split(" ");
        Currency currency = giveEnum(subStr[0]);
        Double amount = Double.parseDouble(subStr[1].substring(1));
        if (subStr[1].charAt(0) == "+".charAt(0)) {
            usersData.addAssetInPortfolio(chatID, currency, amount);
            turnOffEditMode(chatID);
        }
        if (subStr[1].charAt(0) == "-".charAt(0)) {
            usersData.deleteAssetFromPortfolio(chatID, currency, amount);
            turnOffEditMode(chatID);
        }
    }

    private Currency giveEnum(String string) {
        for (Currency s : Currency.values()) {
            if (string.equals(s.toString())) {
                return s;
            }
        }
        return null;
    }

    public String givePortfolio(Long chatID){
        Map portfolio = usersData.getPortfolioFromUser(chatID);
        String string = "Ваш портфель:\n";
        if (portfolio.isEmpty()){
            string = "Ваш портфель пуст. /edit для редактирования";
        }
        for (Object el: portfolio.keySet()){
            string = string + portfolio.get(el).toString() + " " + el.toString() + "\n";
        }
        return string;
    }

    public String getValue(Long chatID){
        currencyMode.setTargetCurrency(chatID, Currency.USD);
        Currency target = currencyMode.getTargetCurrency(chatID);
        Map portfolio = usersData.getPortfolioFromUser(chatID);
        Double value = 0.0;
        for (Object el: portfolio.keySet()){
            currencyMode.setOriginalCurrency(chatID, (Currency) el);
            Currency original = currencyMode.getOriginalCurrency(chatID);
            value = value + currencyConversion.getConversionRatio(original, target) * (Double)portfolio.get(el);
        }
        return String.format(
                "Ценность Вашего портфеля составляет %4.2f %s", value, target);
    }
}
