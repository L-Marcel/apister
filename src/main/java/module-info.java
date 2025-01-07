module app {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.net.http;
    requires javafx.base;
    requires io.github.kamilszewc.javaansitextcolorizer;

    opens app to javafx.fxml;
    exports app;

    opens app.controllers to javafx.fxml;
    exports app.controllers;

    opens app.core to javafx.fxml;
    exports app.core;

    opens app.controllers.components to javafx.fxml;
    exports app.controllers.components;

    opens app.layout to javafx.fxml;
    exports app.layout;
    exports app.interfaces;
}
