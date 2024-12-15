package app.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.errors.InvalidInput;
import app.storage.StorableList;

public class Projects extends StorableList<String> {
    private static Pattern pattern = Pattern.compile("^[\\w\\-. ]+$", 0);
    private static Projects instance = null;

    private Projects() {
        super("projects");
    };

    public static Projects getInstance() {
        if (Projects.instance == null) Projects.instance = new Projects();
        return Projects.instance;
    };
 
    public static void validate(String candidate) throws InvalidInput {
        Projects projects = Projects.getInstance();
        if(candidate == null || candidate.isEmpty()) {
            throw new InvalidInput("Por favor, informe um nome!");
        } else if(projects.get().contains(candidate)) {
            throw new InvalidInput("Já existe um projeto com esse nome!");
        } else if(!pattern.matcher(candidate).matches()) {
            throw new InvalidInput("Caracteres inválidos dectectado!");
        };
    };
};
