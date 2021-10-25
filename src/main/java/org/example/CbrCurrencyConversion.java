package org.example;
import org.example.Currency;
import org.example.CurrencyConversion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CbrCurrencyConversion implements CurrencyConversion {
    @Override
    public double getConversionRatio(Currency original, Currency target) {
        double originalRate = getRate(original);
        double targetRate = getRate(target);
        return originalRate / targetRate;
    }

    @SneakyThrows
    private double getRate(Currency currency) {
        if (currency == Currency.RUB) {
            return 1;
        }
            Document doc = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get();
            Elements trElements = doc.getElementsByTag("table").last().child(0).children();
            for (Element trElement:trElements){
                String value = trElement.child(0).text();
                String vid = Integer.toString(currency.getId());
                if (value.equals(vid)) {
                    String aElement = trElement.children().last().text().replace(",", ".");
                    return Double.parseDouble(aElement);
                }
            };
        return -1;
    }
}