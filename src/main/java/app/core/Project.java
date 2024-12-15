package app.core;

import app.storage.Storable;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;

public class Project extends Storable<Node> {
    public Project(String name) {
        // [NOTE] No futuro precisaremos pensar sobre a validação do nome,
        // mas, por enquanto, ignore essa mensagem. Isso é apenas um lembrete.

        super("project_" + name, new Node("root"));
        this.configNode(this.get());
        this.store();
    };

    private void configNode(Node node) {
        node.valueProperty().addListener((event) -> {
            this.store();
        });

        node.getChildren().addListener((
            ListChangeListener.Change<? extends TreeItem<String>> event
        ) -> {
            if (event.wasAdded() || event.wasReplaced()) {
                event.getAddedSubList().forEach((child) -> {
                    if (child instanceof Node) {
                        this.configNode((Node) child);
                    };
                });
            };

            this.store();
        });
    };
};
