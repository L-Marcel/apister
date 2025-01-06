package app.utils;

import app.core.HeaderEntry;
import app.core.Project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ProjectControllerUtils {
    public static void configLoading(
        Project project,
        Button backButton,
        Label storingLabel,
        ProgressIndicator storingProgressIndicator
    ) {
        storingLabel.setText("Salvo!");

        project.setOnRequestStoring(() -> {
            backButton.setDisable(true);
            storingLabel.setText("Salvando...");
            storingProgressIndicator.setVisible(true);
            storingProgressIndicator.setPrefWidth(15);
            HBox.setMargin(
                storingProgressIndicator, 
                new Insets(0, 7, 0, 0)
            );
        });

        project.setOnStored(() -> {
            backButton.setDisable(false);
            storingLabel.setText("Salvo!");
            storingProgressIndicator.setVisible(false);
            storingProgressIndicator.setPrefWidth(0);
            HBox.setMargin(
                storingProgressIndicator, 
                new Insets(0, 0, 0, 0)
            );
        });
    };

    public static void configPanes(
        TabPane tabPane,
        AnchorPane rightAnchorPane,
        AnchorPane responseAnchorPane,
        TitledPane responseTitledPane,
        TableView<HeaderEntry> headerTableView,
        TableColumn<HeaderEntry, String> keysTableColumn,
        TableColumn<HeaderEntry, String> valuesTableColumn
    ) {
        tabPane.heightProperty().addListener((event, old, current) -> {
            headerTableView.setPrefHeight(current.doubleValue());
            headerTableView.setMaxHeight(current.doubleValue());
        });

        tabPane.widthProperty().addListener((event, old, current) -> {
            headerTableView.setPrefWidth(current.doubleValue());
            headerTableView.setMaxWidth(current.doubleValue());
        });

        rightAnchorPane.widthProperty().addListener((observable, old, current) -> {
            double half = current.doubleValue() / 2.0d;
            keysTableColumn.setPrefWidth(half + 0.5);
            valuesTableColumn.setPrefWidth(half + 0.5);
        });

        responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if(current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
            else responseAnchorPane.setMaxHeight(0);
            headerTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        });
    };
};