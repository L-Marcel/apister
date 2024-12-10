package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import app.log.Log;
import app.secure.ShutdownHook;

public class App extends Application {
    private static Scene scene;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        launch();

        // Needed by the threads and shutdown hook combination
        System.exit(0);
    };

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(load("primary"), 640, 480);
        loadCss("primary");
        stage.setScene(scene);
        stage.show();
    };

    public static void setRoot(String page) throws IOException {
        Log.print("Navigating to " + page);
        scene.setRoot(load(page));
        loadCss(page);
    };

    private static void loadCss(String page) {
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(
            App.class.getResource("global.css").toString(),
            App.class.getResource(page + ".css").toString()
        );
    };

    private static Parent load(String page) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        return loader.load();
    };
};