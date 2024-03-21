package org.CurrencyBot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.CurrencyBot.db.Currency;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Currency chosen = new Currency();
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newBuilder().build();

        String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (resp == null) {
                return;
            }

            Type type = TypeToken.getParameterized(List.class, Currency.class).getType();

            Gson gson = new Gson();
            List<Currency> currencies = gson.fromJson(resp.body(), type);

            currencies.sort(Comparator.comparing(Currency::getCcyNm_UZ));
            StringBuilder currencyInfo = new StringBuilder();
            StringBuilder popularCurInfo = new StringBuilder();
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

            System.out.println(currencyInfo);
            System.out.printf("Popular currencies\n%s", popularCurInfo);
            String breaker = "";
            System.out.println("-1. Stop the program");
            do {
                System.out.print("Insert id of currency: ");
                String idHolder = scanner.nextLine();
                if (idHolder.equals("-1")) {
                    breaker = "-1";
                    continue;
                }
                if (isValid(idHolder)) {
                    breaker = "0";
                    continue;
                }

                int id = Integer.parseInt(idHolder);
                currencyInfo = new StringBuilder();
                for (Currency c : currencies) {
                    if (c.getId().equals(id)) {
                        currencyInfo.append(c.getId()).append(". ")
                                .append(c.getCcyNm_UZ())
                                .append(" (").append(c.getRate()).append(")");
                        chosen = c;
                        break;
                    }
                }

                if (chosen.getId() == null) {
                    System.out.println("Invalid id inserted");
                    breaker = "0";
                }


            } while (!breaker.equals("-1"));


            System.out.println(currencyInfo);
            breaker = "";
            System.out.println("-1. Stop the program");
            String moneyHolder;

            do {
                System.out.print("Insert amount of money: ");
                moneyHolder = scanner.nextLine();
                if (moneyHolder.equals("-1")) {
                    breaker = "-1";
                    continue;
                }
                if (isValid(moneyHolder)) {
                    breaker = "0";
                }


            } while (!breaker.equals("-1"));


            double inserted = Double.parseDouble(moneyHolder);
            double rate = Double.parseDouble(chosen.getRate());
            double holder = inserted / rate;
            String exchanged = String.valueOf(holder);
            int index = exchanged.indexOf(".");
            StringBuilder answer = new StringBuilder("You can get ");

            if (exchanged.substring(index + 1).length() == 1) {
                answer.append(exchanged);
            } else if (exchanged.startsWith("000", index + 1)) {
                answer.append(exchanged, 0, index);
            } else {
                answer.append(exchanged, 0, index + 4);
            }
            answer.append(" ").append(chosen.getCcyNm_EN()).append("(s)");
            System.out.println(answer);
        } catch (Exception e) {
            System.out.println("Error occurred\n" + e.getMessage());
        }
    }

    private static boolean isValid(String holder) {
        for (int i = 0; i < holder.length(); i++) {
            if (!Character.isDigit(holder.charAt(i)) && holder.charAt(i) != '.') {
                System.out.println("Invalid");
                return true;
            }
        }
        return false;
    }
}