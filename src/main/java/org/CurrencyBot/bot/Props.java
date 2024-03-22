package org.CurrencyBot.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Props {
    public static String get(String key, String path) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(path));
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
