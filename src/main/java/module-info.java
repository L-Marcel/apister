module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.net.http;

    opens app to javafx.fxml;
    exports app;

    opens app.controllers to javafx.fxml;
    exports app.controllers;

    opens app.layout to javafx.fxml;
    exports app.layout;
}
