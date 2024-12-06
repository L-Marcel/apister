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
        stage.setScene(scene);
        stage.show();
    };

    static void setRoot(String fxml) throws IOException {
        Log.print("Navigating to " + fxml);
        scene.setRoot(load(fxml));
    };

    private static Parent load(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    };
};