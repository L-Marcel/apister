package app.layout;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

public class CustomTextAreaSkin extends TextAreaSkin {
    public CustomTextAreaSkin(TextArea textArea) {
        super(textArea);

        Parent content = this.getContent();
        Path caretPath = (Path) this.getContent().getChildrenUnmodifiable().get(content.getChildrenUnmodifiable().size() - 1);

        caretPath.fillProperty().unbind();
        caretPath.fillProperty().set(Color.valueOf("#E2E2E2"));

        caretPath.strokeProperty().unbind();
        caretPath.strokeProperty().set(Color.valueOf("#E2E2E2"));

        caretPath.strokeWidthProperty().unbind();
        caretPath.strokeWidthProperty().set(1);
    };

    public ScrollPane getTextAreaScrollPane() {
        return (ScrollPane) this.getChildren().get(0);
    };

    public Parent getContent() {
        return (Parent) this.getTextAreaScrollPane().getContent();
    };

    public double getContentHeight() {
        return this.getContent().getLayoutBounds().getHeight();
    };

    public double getContentWidth() {
        return this.getContent().getLayoutBounds().getWidth();
    };
};