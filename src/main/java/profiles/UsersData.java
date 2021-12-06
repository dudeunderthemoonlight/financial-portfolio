package profiles;

import currency.Currency;
import java.util.HashMap;
import java.util.Map;


public class UsersData {

    private final Map<Long, User> users = new HashMap<>();

    public boolean inData(long chatID) {
        return users.containsKey(chatID);
    }

    public void addUser(Long chatID) {
        User user = new User(chatID);
        users.put(chatID, user);
    }

    public void addAssetInPortfolio(Long chatID, Currency currency, Double amount){
        User user = users.get(chatID);
        InvestmentPortfolio portfolio = user.getUserPortfolio();
        portfolio.addAsset(currency, amount);
    }

    public void deleteAssetFromPortfolio(Long chatID, Currency currency, Double amount){
        User user = users.get(chatID);
        InvestmentPortfolio portfolio = user.getUserPortfolio();
        portfolio.deleteAsset(currency, amount);
    }

    public Map getPortfolioFromUser(Long chatID){
        return users.get(chatID).getUserPortfolio().getPortfolio();
    }
}
