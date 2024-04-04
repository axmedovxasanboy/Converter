package handlers;

import bean.Steps;
import bot.Bot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;

import static db.Instances.db;

public class MessageHandler implements BaseHandler {
    private final SendMessage message = new SendMessage();

    @Override
    public void handle(Update update, Bot bot) {
        Message userMessage = update.getMessage();
        Long chatId = userMessage.getChatId();

        HashMap<Long, Contact> contacts = db.getContacts();
        Contact contact = contacts.getOrDefault(chatId, null);

        message.setChatId(chatId);

        if (userMessage.hasContact()) {
            contacts.put(chatId, userMessage.getContact());
            contact = userMessage.getContact();
            Steps.set(chatId, "Contact accepted");
            System.out.println(contact);
        }

        sendMenu(chatId, contact, userMessage, message);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    private void sendMenu(Long chatId, Contact contact, Message userMessage, SendMessage message) {
        String text = userMessage.getText();
        String step = Steps.get(chatId);

        if (step.equals("main")) {
            if (contact == null) contactKeyboard(message);
            if (text.equals("/start")) {

                if (contact == null) {
                    message.setText("Bizning telegram botimizga xush kelibsiz. Botdan to'liq foydalanish uchun quyidagi tugma yordamida kontaktingizni jo'nating");
                } else {
                    exchangeCallback(message);
                }
            }
        } else if (step.equals("Contact accepted")) {
            String name = contact.getFirstName();
            message.setText("Kontaktingiz tasdiqlandi, " + name);
            exchangeCallback(message);
        }

    }

    private void exchangeCallback(SendMessage message) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton b1 = new InlineKeyboardButton();
        b1.setText("Valyuta ayirboshlash");
        b1.setCallbackData("exchange");
        keyboard.setKeyboard(List.of(List.of(b1)));

        message.setReplyMarkup(keyboard);

    }


    private void contactKeyboard(SendMessage message) {
        ReplyKeyboardMarkup keyboard = getContactKeyboard();

        message.setReplyMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getContactKeyboard() {
        KeyboardButton btn1 = new KeyboardButton();
        KeyboardRow row1 = new KeyboardRow();
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        btn1.setRequestContact(true);
        btn1.setText("\uD83D\uDCDE Jo'natish");
        row1.add(btn1);

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(List.of(row1));
        return keyboard;
    }
}
