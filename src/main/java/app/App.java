package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        loadFonts();
        scene = new Scene(load("projects"), 640, 480);
        applyCss("projects");
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.show();
    };

    public static void setRoot(String page) throws IOException {
        Log.print("Navigating to " + page);
        scene.setRoot(load(page));
        applyCss(page);
    };

    private static void applyCss(String page) {
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(
            App.class.getResource("global.css").toExternalForm(),
            App.class.getResource(page + ".css").toExternalForm()
        );
    };

    public static void applyCss(Parent parent, String page) {
        parent.getStylesheets().clear();
        parent.getStylesheets().addAll(
            App.class.getResource("global.css").toExternalForm(),
            App.class.getResource(page + ".css").toExternalForm()
        );
    };

    public static Parent load(String page, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        loader.setController(controller);
        return loader.load();
    };

    public static Parent load(String page) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        return loader.load();
    };

    private static void loadFonts() {
        String[] fonts = {
            "Oswald/Oswald-Bold.ttf",
            "Oswald/Oswald-ExtraLight.ttf",
            "Oswald/Oswald-Light.ttf",
            "Oswald/Oswald-Medium.ttf",
            "Oswald/Oswald-Regular.ttf",
            "Oswald/Oswald-SemiBold.ttf",
            // "Oswald/Oswald.ttf",
            "Quattrocento/Quattrocento-Bold.ttf",
            "Quattrocento/Quattrocento-Regular.ttf"
        };

        for (String font : fonts) {
            try {
                Font.loadFont(
                    Files.newInputStream(
                        Path.of(
                            App.class.getResource("fonts/" + font).toURI()
                        )
                    ),
                13);
            } catch (Exception e) {
                e.printStackTrace();
            }; 
        };
    };
};