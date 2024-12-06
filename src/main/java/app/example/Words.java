package app.example;

import app.storage.StorableList;

public class Words extends StorableList<String> {
    private static Words instance;

    private Words() {
        super("words");
    };

    public static Words getInstance() {
        if (Words.instance == null) Words.instance = new Words();
        return Words.instance;
    };
};
