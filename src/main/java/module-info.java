module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.net.http;

    opens app to javafx.fxml;
    exports app;
}
