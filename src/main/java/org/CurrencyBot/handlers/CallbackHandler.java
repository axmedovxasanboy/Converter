package org.CurrencyBot.handlers;

import org.CurrencyBot.bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackHandler implements BaseHandler{
    @Override
    public void handle(Update update, Bot bot) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        callbackQuery.getData();


    }
}
