package org.CurrencyBot.db;

import lombok.Getter;
import lombok.Setter;
import org.CurrencyBot.db.Database;

@Getter
@Setter
public class Instances {
    public static Database db = new Database();
}
