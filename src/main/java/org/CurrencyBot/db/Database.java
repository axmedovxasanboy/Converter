package org.CurrencyBot.db;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Database {
    protected List<Currency> currencies = new ArrayList<>();
    protected String currency;
    protected String popularCurrency;

    @Override
    public String toString() {
        return "Database{" +
                "currencies=" + currencies +
                ", currency='" + currency + '\'' +
                ", popularCurrency='" + popularCurrency + '\'' +
                '}';
    }
}

