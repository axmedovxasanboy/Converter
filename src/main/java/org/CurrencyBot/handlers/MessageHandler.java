package org.CurrencyBot.handlers;

import lombok.Getter;
import lombok.Setter;
import org.CurrencyBot.bot.Bot;
import org.CurrencyBot.db.Currency;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.CurrencyBot.db.Instances.db;

@Getter
@Setter
public class MessageHandler implements BaseHandler {
    private Contact contact;
    private Currency chosen;
    private Boolean currencyIdInserted;

    @Override
    public void handle(Update update, Bot bot) {
        Message userMsg = update.getMessage();

        String userText = userMsg.getText();

        SendMessage message = new SendMessage();
        SendMessage message1 = new SendMessage();
        message.setChatId(userMsg.getChatId());
        message1.setChatId(userMsg.getChatId());
        if (userMsg.hasContact()) {
            contact = userMsg.getContact();
            System.out.println(contact);
        }


        if (userText != null) {
            if (contact == null) contactBtn(message);

            if (userText.equals("/start")) {

                setCurrencyIdInserted(false);
                if (contact == null) {
                    message.setText("Bizning telegram botimizga xush kelibsiz. Botdan to'liq foydalanish uchun quyidagi tugma yordamida kontaktingizni jo'nating");
                } else {
                    currencyList(bot, message);
                }
            } else if (contact != null && isValidCurrencyId(userText, message)) {

                setCurrencyIdInserted(true);
                try {
                    bot.execute(message);
                    message.setText("Pul mablag'ini kiriting");
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            } else if (getCurrencyIdInserted()) {
                if (IsValidMoney(userText)) {

                    double inserted = Double.parseDouble(userText);
                    double rate = Double.parseDouble(chosen.getRate());
                    double holder = inserted / rate;
                    String exchanged = String.valueOf(holder);
                    if (exchanged.contains("E")) {
                        message.setText("Kiritilgan mablag' juda kichkina");
                    } else {
                        int index = exchanged.indexOf(".");
                        StringBuilder answer = new StringBuilder("Siz ");

                        if (exchanged.substring(index + 1).length() == 1) {
                            answer.append(exchanged);
                        } else if (exchanged.startsWith("000", index + 1)) {
                            answer.append(exchanged, 0, index);
                        } else {
                            answer.append(exchanged, 0, index + 4);
                        }
                        answer.append(" ").append(chosen.getCcyNm_UZ()).append(" olib bilasiz");
                        message.setText(String.valueOf(answer));
                    }
                } else if (userText.equals("\uD83D\uDED1 Stop")) {
                    currencyList(bot, message);

                } else {
                    message.setText("Yaroqsiz pul mablag'i kiritildi");
                }


            } else if (contact == null) {
                message.setText("Iltimos, birinchi navbatda kontaktingizni jo'nating");
            }

        } else if (contact != null) {
            currencyList(bot, message);
        }

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    private void currencyList(Bot bot, SendMessage message) {
        String[] text = {"Kontaktingiz tasdiqlandi " + contact.getFirstName(), db.getCurrency(), db.getPopularCurrency()};
        for (int i = 0; i < 3; i++) {
            message.setText(text[i]);
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        message.setText("Valyuta ID sini kiriting");
        start(message);
    }

    private boolean IsValidMoney(String money) {
        for (int i = 0; i < money.length(); i++) {
            if (!Character.isDigit(money.charAt(i)) && money.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    private boolean isValidCurrencyId(String id, SendMessage message) {
        for (int i = 0; i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i))) return false;
        }
        StringBuilder currencyInfo = new StringBuilder();
        int currencyId = Integer.parseInt(id);
        List<Currency> currencies = db.getCurrencies();
        for (Currency c : currencies) {
            if (c.getId().equals(currencyId)) {
                currencyInfo.append(c.getId()).append(". ")
                        .append(c.getCcyNm_UZ())
                        .append(" (").append(c.getRate()).append(")");
                setChosen(c);
                message.setText(String.valueOf(currencyInfo));
                return true;
            }
        }

        return false;
    }

    private void start(SendMessage message) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(getMenuKeyboard());

        message.setReplyMarkup(keyboard);
    }

    private List<KeyboardRow> getMenuKeyboard() {
        KeyboardButton btn1 = new KeyboardButton();
        btn1.setText("\uD83D\uDED1 Stop");


        KeyboardRow row1 = new KeyboardRow();
        row1.add(btn1);
        return List.of(row1);

    }

    private void contactBtn(SendMessage message) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(getContactKeyboard());

        message.setReplyMarkup(keyboard);
    }

    private List<KeyboardRow> getContactKeyboard() {
        KeyboardButton btn1 = new KeyboardButton();
        btn1.setRequestContact(true);
        btn1.setText("\uD83D\uDCDE Jo'natish");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(btn1);
        return List.of(row1);
    }
}
