package org.CurrencyBot.handlers;

import org.CurrencyBot.bot.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestChat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestUser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MessageHandler implements BaseHandler {
    @Override
    public void handle(Update update, Bot bot) {
        Message userMsg = update.getMessage();
        SendMessage message = new SendMessage();
        message.setChatId(userMsg.getChatId());
        Contact contact = userMsg.getContact();
        System.out.println("24-row " + contact);

        if (userMsg.getText().equals("/start")) {

            message.setText("Welcome to our bot. Share your contact using button below to use bot fully");
        } else if (contact != null) {
            System.out.println("32-row" + contact);
            message.setText("Hello " + contact.getFirstName());

        } else {
            message.setText("Please, share your contact first");
        }
        contactBtn(message);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    private void contactBtn(SendMessage message) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(getKeyBoard());

        message.setReplyMarkup(keyboard);
    }

    private List<KeyboardRow> getKeyBoard() {
        KeyboardButton btn1 = new KeyboardButton();
        btn1.setRequestContact(true);
        btn1.setText("\uD83D\uDCDEShare");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(btn1);
        return List.of(row1);
    }
}
