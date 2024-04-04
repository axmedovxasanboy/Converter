package bot;

import handlers.BaseHandler;
import handlers.CallbackHandler;
import handlers.MessageHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

    private BaseHandler messageHandler;
    private BaseHandler callbackHandler;

    public Bot(){
        messageHandler = new MessageHandler();
        callbackHandler = new CallbackHandler();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageHandler.handle(update, this);
        }else{
            callbackHandler.handle(update, this);
        }
    }

    @Override
    public String getBotUsername() {
        return Props.get("bot.username", "resources/bot.properties");
    }

    public String getBotToken() {
        return Props.get("bot.token", "resources/bot.properties");
    }
}
