package db;

import bean.Currency;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Database {
    protected List<Currency> currencies = new ArrayList<>();
    protected String currency;
    protected String popularCurrency;
    protected HashMap<Long, Contact> contacts = new HashMap<>();

    @Override
    public String toString() {
        return "Database{" +
                "currencies=" + currencies +
                ", currency='" + currency + '\'' +
                ", popularCurrency='" + popularCurrency + '\'' +
                '}';
    }
}

