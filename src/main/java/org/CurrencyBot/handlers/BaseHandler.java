package org.CurrencyBot.handlers;

import org.CurrencyBot.bot.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BaseHandler {
    void handle(Update update, Bot bot);
}
