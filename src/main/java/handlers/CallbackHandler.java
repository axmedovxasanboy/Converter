package handlers;

import bot.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CallbackHandler implements BaseHandler {
    private final SendMessage message = new SendMessage();

    @Override
    public void handle(Update update, Bot bot) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        message.setChatId(chatId);

    }
}
