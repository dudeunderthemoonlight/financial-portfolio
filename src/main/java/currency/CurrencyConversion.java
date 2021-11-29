package currency;

public interface CurrencyConversion {

    static CurrencyConversion getInstance() {
        return new CbrCurrencyConversion();
    }

    double getConversionRatio(Currency original, Currency target);
}