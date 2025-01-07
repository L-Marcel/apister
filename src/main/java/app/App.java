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
        Application.launch(args);
        System.exit(0);
    };

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(load("projects"), 640, 480);
        App.applyCss("projects");
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    };

    public static Scene getScene() {
        return App.scene;
    };

    //#region Root
    public static void setRoot(String page) throws IOException {
        Log.print("Navigating to " + page + "...");
        App.scene.setRoot(load(page));
        App.applyCss(page);
    };

    public static void setRoot(String page, Object controller) throws IOException {
        Log.print("Navigating to " + page + "...");
        App.scene.setRoot(load(page, controller));
        App.applyCss(page);
    };
    //#endregion
    //#region Style
    private static void applyCss(String page) {
        App.scene.getStylesheets().clear();
        App.scene.getStylesheets().addAll(
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
    //#endregion
    //#region Load
    public static Parent load(String page, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        loader.setController(controller);
        return loader.load();
    };

    public static Parent load(String page) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        return loader.load();
    };
    //#endregion
};