package profiles;

import currency.Currency;
import java.util.HashMap;
import java.util.Map;


public class InvestmentPortfolio {

    private final Map<Currency, Double> portfolio = new HashMap<>();

    public void addAsset(Currency currency, Double amount) {
        Double newAmount = amount + portfolio.getOrDefault(currency, 0.0);
        portfolio.put(currency, newAmount);
        if (newAmount == 0.0) {
            portfolio.remove(currency);
        }
    }

    public  void deleteAsset(Currency currency, Double amount) {
        Double newAmount = portfolio.getOrDefault(currency, 0.0) - amount;
        portfolio.put(currency, newAmount);
        if (newAmount == 0.0) {
            portfolio.remove(currency);
        }
    }

    public Map getPortfolio(){
        return portfolio;
    }
}
