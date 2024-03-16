package org.ATM;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

            // currencies.forEach(System.out::println);

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
            System.out.print("Insert id of currency: ");
            String idHolder = scanner.nextLine();
            if (isValid(idHolder)) return;

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
            System.out.println(currencyInfo);
            System.out.print("Insert amount of money: ");
            String moneyHolder = scanner.nextLine();
            if (isValid(moneyHolder)) return;


            Double money = Double.parseDouble(moneyHolder);
            System.out.println(money);
            System.out.println(Double.parseDouble(chosen.getRate()));
//            Oldin / bilan bolish
//            Keyin % bilan qoldiqni olib yaxlitlash

            System.out.println(Math.round((money / Double.parseDouble(chosen.getRate()))*100)/100);

//            BigDecimal money1 = new BigDecimal(money);
//            money1.setScale(2, )


            System.out.println("You can get " + (money / Double.parseDouble(chosen.getRate())) + " " + chosen.getCcyNm_EN() + "(s)");


        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
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