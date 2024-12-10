package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import app.example.Words;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Primary implements Initializable {
    @FXML private ListView<String> wordList;
    @FXML private TextField worldField;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        Words words = Words.getInstance();
        wordList.setItems(words.get());
    };

    @FXML
    private void onPressEnterToSubmit(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            addWord();
        };
    };

    @FXML
    private void onDoubleClickToRemove(MouseEvent e) {
        if (e.getClickCount() >= 2) {
            removeWord();
        };
    };

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    };

    @FXML
    private void removeWord() {
        int index = wordList.getSelectionModel().getSelectedIndex();
        if(index >= 0) {
            wordList.getSelectionModel().clearSelection();
            Words words = Words.getInstance();
            words.removeByIndex(index);
        };
    };

    @FXML
    private void addWord() {
        Words words = Words.getInstance();
        String text = worldField.getText();
        if (!text.isBlank()) {
            words.add(text);
            worldField.clear();
            worldField.requestFocus();
        };
    };
};
