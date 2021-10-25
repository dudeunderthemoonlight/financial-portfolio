package org.example;

import org.example.Currency;
import org.example.HashMapCurrencyMode;

public interface CurrencyMode {

    static CurrencyMode getInstance() {
        return new HashMapCurrencyMode();
    }

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currency currency);

    void setTargetCurrency(long chatId, Currency currency);
}