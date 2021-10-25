package org.example;

import org.example.Currency;
import org.example.CbrCurrencyConversion;

public interface CurrencyConversion {

    static CurrencyConversion getInstance() {
        return new CbrCurrencyConversion();
    }

    double getConversionRatio(Currency original, Currency target);
}