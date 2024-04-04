package bot;

import bean.ReadThread;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new ReadThread());
        thread1.start();

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }
}
