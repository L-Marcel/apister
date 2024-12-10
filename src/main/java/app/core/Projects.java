package app.core;

import app.storage.Storable;

public class Projects extends Storable<Node> {
    private static Projects instance = null;

    private Projects() {
        super("projects", new Node("root"));
        // [TODO] Adicione arquivo evento recursivo que
        // chama quando necessário o método de armazenamento.
    };

    public static Projects getInstance() {
        if (Projects.instance == null) Projects.instance = new Projects();
        return Projects.instance;
    };
};
