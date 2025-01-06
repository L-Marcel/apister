package app.core;

import app.storage.Storable;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;

public class Project extends Storable<Node> {
    public Project(String name) {
        super("project_" + name, new Node("root"));
        this.configNode(this.get());
        this.store();
    };

    public Project(String name, Node node) {
        super("project_" + name, node);
        this.configNode(this.get());
        this.store();
    };

    private void configNode(Node node) {
        if(node instanceof Request) {
            Request request = (Request) node;

            request.bodyProperty().addListener((event) -> {
                this.store();
            });

            request.urlProperty().addListener((event) -> {
                this.store();
            });

            request.headerProperty().addListener((
                ListChangeListener.Change<? extends HeaderEntry> event
            ) -> {
                while(event.next()) {
                    if(event.wasAdded() || event.wasReplaced()) {
                        event.getAddedSubList().forEach((child) -> {
                            child.keyProperty().addListener((keyEvent) -> {
                                this.store();
                            });

                            child.valueProperty().addListener((valueEvent) -> {
                                this.store();
                            });
                        });
                    };
                };

                this.store();
            });

            request.headerProperty().forEach((entry) -> {
                entry.keyProperty().addListener((event) -> {
                    this.store();
                });

                entry.valueProperty().addListener((event) -> {
                    this.store();
                });
            });

            request.lastResponseProperty().addListener((event) -> {
                this.store();
            });
        };

        node.getChildren().addListener((
            ListChangeListener.Change<? extends TreeItem<String>> event
        ) -> {
            while(event.next()) {
                if(event.wasAdded() || event.wasReplaced()) {
                    event.getAddedSubList().forEach((child) -> {
                        if(child instanceof Node) {
                            this.configNode((Node) child);
                        };
                    });
                };
            };

            this.store();
        });

        node.getChildren().forEach((child) -> {
            if(child instanceof Node) {
                this.configNode((Node) child);
            };
        });
    };
};
