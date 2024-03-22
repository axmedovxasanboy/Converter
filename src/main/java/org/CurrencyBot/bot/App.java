package org.CurrencyBot.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.CurrencyBot.db.Instances.db;

public class App {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> currencies = executor.submit(new ReadCurrencies());
        String answer = null;
        try {
            answer = currencies.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        if (answer != null) {
            if (answer.startsWith("Error")) {
                System.out.println("Error occurred while getting currencies");
            } else {
                String[] split = answer.split("<popular>");
                db.setCurrency(split[0]);
                db.setPopularCurrency(split[1]);
                System.out.println(db);
            }

        }

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }
}
