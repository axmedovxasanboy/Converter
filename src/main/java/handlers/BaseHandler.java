package handlers;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BaseHandler {
    void handle(Update update, Bot bot);
}
