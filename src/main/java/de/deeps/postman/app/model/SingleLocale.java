package de.deeps.postman.app.model;

import lombok.Setter;

import java.util.ResourceBundle;

public class SingleLocale {

    @Setter private static ResourceBundle locale;
    @Setter private static SingleLocale instance = new SingleLocale();

    public static ResourceBundle get() {
        return getInstance().getBasicLocale();
    }

    public static void set(ResourceBundle locale) {
        getInstance().setBasicLocale(locale);
    }

    private static synchronized SingleLocale getInstance() {
        if (!isSingleLocaleAlreadyExisting()) {
            setInstance(new SingleLocale());
        }
        return getBasicInstance();
    }

    private static boolean isSingleLocaleAlreadyExisting(){
        return getBasicInstance() != null;
    }

    private static SingleLocale getBasicInstance(){
        return instance;
    }

    private void setBasicLocale(ResourceBundle locale) {
        SingleLocale.locale = locale;
    }

    private ResourceBundle getBasicLocale() {
        return locale;
    }
}
