package app.core;

import app.storage.Storable;

public class Project extends Storable<Node> {
    private Project(String name) {
        // [NOTE] No futuro precisaremos pensar sobre a validação do nome,
        // mas, por enquanto, ignore essa mensagem. Isso é apenas um lembrete.

        super("project_" + name, new Node("root"));
        // [TODO] Adicione arquivo evento recursivo que
        // chama quando necessário o método de armazenamento.
    };
};
