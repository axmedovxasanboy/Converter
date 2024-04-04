package bean;

import bot.Props;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

import static db.Instances.db;

public class ReadThread implements Runnable {
    @Override
    public void run() {
        Gson gson = new Gson();
        StringBuilder currencyInfo = new StringBuilder(), popularCurInfo = new StringBuilder();

        String api = Props.get("api", "resources/currency.properties");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request;

        try {
            if (api != null) {
                request = HttpRequest.newBuilder()
                        .uri(new URI(api))
                        .GET()
                        .build();
            } else {
                db.setCurrency("Error (api=null)");
                return;
            }
        } catch (URISyntaxException e) {
            db.setCurrency("Error (in request) " + e.getMessage());
            return;
        }

        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            db.setCurrency("Error (in request) " + e.getMessage());
            return;
        }
        Type type = TypeToken.getParameterized(List.class, Currency.class).getType();

        List<Currency> currencies = gson.fromJson(resp.body(), type);
        currencies.sort(Comparator.comparing(Currency::getUZ));
        db.setCurrencies(currencies);

        int[] popularCurId = {1, 13, 14, 15, 21, 22, 37, 57, 69, 73};
        for (Currency currency : currencies) {
            for (int id : popularCurId) {
                if (currency.getId().equals(id)) {
                    popularCurInfo.append(currency.getId()).
                            append(". ").
                            append(currency.getUZ()).
                            append(" (").append(currency.getRate()).append(")").
                            append("\n");
                }
            }
            currencyInfo.append(currency.getId()).
                    append(". ").
                    append(currency.getUZ()).
                    append(" (").append(currency.getRate()).append(")").
                    append("\n");
        }
        db.setCurrency(String.valueOf(currencyInfo));
        db.setPopularCurrency(String.valueOf(popularCurInfo));
    }
}
