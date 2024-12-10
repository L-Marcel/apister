package app.core;

import app.storage.StorableList;

public class Projects extends StorableList<String> {
    private static Projects instance = null;

    private Projects() {
        super("projects");
    };

    public static Projects getInstance() {
        if (Projects.instance == null) Projects.instance = new Projects();
        return Projects.instance;
    };
};
