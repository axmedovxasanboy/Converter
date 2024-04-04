package handlers;

import bean.Currency;
import bean.Steps;
import bot.Bot;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

@Getter
@Setter
public class MessageHandler1 implements BaseHandler {
    private Currency chosen;
    private Boolean currencyIdInserted;
    private Message userMessage;
    private SendMessage message = new SendMessage();
    private Long chatId;
    private TelegramLongPollingBot bot;

    @Override
    public void handle(Update update, Bot bot) {
        this.bot = bot;
        userMessage = update.getMessage();
        chatId = userMessage.getChatId();
        message.setChatId(chatId);
        HashMap<Long, Contact> contacts = db.getContacts();
        Contact contact = contacts.getOrDefault(chatId, null);

        if (userMessage.hasContact()) {
            contacts.put(chatId, userMessage.getContact());
            contact = userMessage.getContact();
            Steps.set(chatId, "Contact accepted");
            System.out.println(contact);
        }

        mainMenu(chatId, userMessage, contact, message, bot);

//        if (userText != null) {
//            if (contact == null) contactBtn(message);
//
//            if (userText.equals("/start")) {
//
//
//                if (contact == null) {
//                    message.setText("Bizning telegram botimizga xush kelibsiz. Botdan to'liq foydalanish uchun quyidagi tugma yordamida kontaktingizni jo'nating");
//                } else {
//                    currencyList(bot, message);
//                }
//            } else if (contact != null && isValidCurrencyId(userText, message)) {
//                test(message);
//
//
//                setCurrencyIdInserted(true);
//                try {
//                    bot.execute(message);
//                    message.setText("Pul mablag'ini kiriting");
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                    System.out.println(e.getMessage());
//                }
//
//            } else if (currencyIdInserted != null && getCurrencyIdInserted()) {
//                if (IsValidMoney(userText)) {
//
//                    double inserted = Double.parseDouble(userText);
//                    double rate = Double.parseDouble(chosen.getRate());
//                    double holder = inserted / rate;
//                    String exchanged = String.valueOf(holder);
//                    if (exchanged.contains("E")) {
//                        message.setText("Kiritilgan mablag' juda kichkina");
//                    } else {
//                        int index = exchanged.indexOf(".");
//                        StringBuilder answer = new StringBuilder("Siz ");
//
//                        if (exchanged.substring(index + 1).length() == 1) {
//                            answer.append(exchanged);
//                        } else if (exchanged.startsWith("000", index + 1)) {
//                            answer.append(exchanged, 0, index);
//                        } else {
//                            answer.append(exchanged, 0, index + 4);
//                        }
//                        answer.append(" ").append(chosen.getUZ()).append(" olib bilasiz");
//                        message.setText(String.valueOf(answer));
//                    }
//                } else if (userText.equals("Stop")) {
//                    currencyList(bot, message);
//
//                } else {
//                    message.setText("Yaroqsiz pul mablag'i kiritildi");
//                }
//
//
//            } else if (contact == null) {
//                message.setText("Iltimos, birinchi navbatda kontaktingizni jo'nating");
//            } else if (userText.equals("Stop")) {
//                currencyList(bot, message);
//
//            } else {
//                message.setText("Yaroqsiz pul mablag'i kiritildi");
//            }
//
//        } else if (contact != null) {
//            currencyList(bot, message);
//        }

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    private void mainMenu(Long chatId, Message userMsg, Contact contact, SendMessage message, Bot bot) {
        String step = Steps.get(chatId);
        String text = userMsg.getText();

        if (step.equals("main")) {
            if (contact == null) contactBtn(message);
            if (text.equals("/start")) {

                if (contact == null) {
                    message.setText("Bizning telegram botimizga xush kelibsiz. Botdan to'liq foydalanish uchun quyidagi tugma yordamida kontaktingizni jo'nating");
                } else {
                    exchangeCallback(chatId, message);
                }
            }

        } else if (step.equals("Contact accepted")) {
            contactAccepted(chatId, message, contact, bot);
        }

    }


    private void contactAccepted(Long chatId, SendMessage message, Contact contact, Bot bot) {
        message.setChatId(chatId);
        String name = contact.getFirstName();
        message.setText("Kontaktingiz tasdiqlandi, " + name);
        exchangeCallback(chatId, message);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        exchangeCallback(chatId, message);
    }

    private void exchangeCallback(Long chatId, SendMessage message) {
        message.setChatId(chatId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton b1 = new InlineKeyboardButton();
        b1.setText("Valyuta ayirboshlash");
        b1.setCallbackData("exchange");
        keyboard.setKeyboard(List.of(List.of(b1)));

        message.setReplyMarkup(keyboard);

    }


    private void exchangeMenu(Long chatId, SendMessage message, Contact contact, Bot bot) {
        EditMessageText editMsg = new EditMessageText();
        editMsg.setChatId(chatId);
        message.setChatId(chatId);
        List<Currency> currencies = db.getCurrencies();
        int pages = currencies.size() / 10;
        int redundant = currencies.size() % 10;
        StringBuilder info = new StringBuilder();
        Currency c;
        int size = currencies.size();
        int counter = 0;
        while (counter != currencies.size()) {


            counter++;

        }
        for (int i = 1; i <= pages; i++) {
            for (int j = 0; j < 10; j++) {
                c = currencies.get(j);
                info.append((j + 1)).
                        append(") ").append(c.getUZ()).
                        append(" (").append(c.getExt()).
                        append(") - ").append(c.getRate());
            }

        }

    }

    private void test(SendMessage message) {
        KeyboardButton btn1 = new KeyboardButton();
//        KeyboardButton btn2 = new KeyboardButton();
//        KeyboardButton btn3 = new KeyboardButton();
        btn1.setText("Stop");
//        btn2.setText("Stop");
//        btn3.setText("Stop");


        KeyboardRow row1 = new KeyboardRow();
//        KeyboardRow row2 = new KeyboardRow();
        row1.add(btn1);
//        row1.add(btn2);
//        row2.add(btn3);
        List<KeyboardRow> rows = List.of(row1/*, row2*/);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
    }

    private void currencyList(Bot bot, SendMessage message) {
        String[] text = {"Kontaktingiz tasdiqlandi " + /*contact.getFirstName(),*/ db.getCurrency(), db.getPopularCurrency()};
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
//        start(message);
        test(message);
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
                        .append(c.getUZ())
                        .append(" (").append(c.getRate()).append(")");
                setChosen(c);
                message.setText(String.valueOf(currencyInfo));
                return true;
            }
        }

        return false;
    }


    private void contactBtn(SendMessage message) {
        ReplyKeyboardMarkup keyboard = getContactKeyboard();

        message.setReplyMarkup(keyboard);
    }

    private static ReplyKeyboardMarkup getContactKeyboard() {
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
