package app.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import app.layout.TableCellTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;

    @FXML private TableView<Map.Entry<String, String>> headerTableView;
    @FXML private TableColumn<Map.Entry<String, String>, Integer> keysTableColumn;
    @FXML private TableColumn<Map.Entry<String, String>, Integer> valuesTableColumn;
    @FXML private AnchorPane responseAnchorPane;
    @FXML private TitledPane responseTitledPane;
    @FXML private TreeView<String> treeView;

    public ProjectController(String name) {
        this.project = new Project(name);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.treeView.setRoot(this.project.get());
        this.responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if (current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
            else responseAnchorPane.setMaxHeight(0);
        });

        // [TIP] A célula vai receber como valor o índice da parzinho de strings.
        // Se passar o valor da chave, por exemplo, vai ficar atualizando 
        // direto ao digitar. Vamos precisar armazena o Header em um List<Pair<String, String>>.
        // Digo, não vejo outra alternativa, fique a vontade para tenta/pensar.

        this.keysTableColumn.setCellFactory(cell -> new TableCellTextField());
        this.keysTableColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(0).asObject());
        this.valuesTableColumn.setCellFactory(cell -> new TableCellTextField());
        this.valuesTableColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(0).asObject());

        // [TIP] Parte estática para testes
        
        Request req = new Request("abc");
        req.getHeaders().put("Authorization", "Bearer");
        this.select(req);
    };

    public void select(Node node) {
        if(node instanceof Request) {
            this.request = (Request) node;
            
            // [TIP] Isso aqui vai ter que mudar.
            // Observação: criar um objeto observável e alterar ele
            // não chama evento no observável, mas, aparentemente,
            // alterar no observável chama o evento e altera
            // na instância do original. É que os observáveis são
            // supostamente Wrappers, e nada mais.

            this.headerTableView.setItems(
                FXCollections.observableArrayList(
                    request.getHeaders().entrySet()
                )
            );
        };
    };

    @FXML 
    public void back() {
        try {
            App.setRoot("projects");
        } catch (Exception e) {
            e.printStackTrace();
        };
    };
};
