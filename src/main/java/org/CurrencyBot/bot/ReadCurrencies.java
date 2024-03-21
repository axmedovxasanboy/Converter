package org.CurrencyBot.bot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.CurrencyBot.db.Currency;
import org.CurrencyBot.db.Database;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class ReadCurrencies implements Callable<String> {
    @Override
    public String call() {
        Gson gson = new Gson();
        Database db = new Database();
        StringBuilder currencyInfo = new StringBuilder(), popularCurInfo = new StringBuilder();

        String api = Props.get("api");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request;

        try {
            if (api != null) {
                request = HttpRequest.newBuilder()
                        .uri(new URI(api))
                        .GET()
                        .build();
            } else return "Error (api=null)";
        } catch (URISyntaxException e) {
            return "Error (in request) " + e.getMessage();
        }

        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return "Error (in response) " + e.getMessage();
        }
        Type type = TypeToken.getParameterized(List.class, Currency.class).getType();

        List<Currency> currencies = gson.fromJson(resp.body(), type);
        currencies.sort(Comparator.comparing(Currency::getCcyNm_UZ));
        db.setCurrencies(currencies);

        int[] popularCurId = {1, 13, 14, 15, 21, 22, 37, 57, 69, 73};
        for (Currency currency : currencies) {
            for (int id : popularCurId) {
                if (currency.getId().equals(id)) {
                    popularCurInfo.append(currency.getId()).
                            append(". ").
                            append(currency.getCcyNm_UZ()).
                            append(" (").append(currency.getRate()).append(")").
                            append("\n");
                }
            }
            currencyInfo.append(currency.getId()).
                    append(". ").
                    append(currency.getCcyNm_UZ()).
                    append(" (").append(currency.getRate()).append(")").
                    append("\n");
        }
        return currencyInfo + "|popular>" + popularCurInfo;
    }
}
