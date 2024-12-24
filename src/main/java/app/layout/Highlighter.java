package app.layout;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class Highlighter {
    protected final Pattern KEY_REG = Pattern.compile("^([\\w-]+)(?:)", Pattern.MULTILINE);
    protected final Pattern STRING_REG = Pattern.compile("(\\\"([^\\\"]*)\\\"|\\'.\\'|\\'\\')");
    protected final Pattern BOOLEAN_REG = Pattern.compile("(true|false)");
    protected final Pattern NUMBER_REG = Pattern.compile("(\\d)");

    protected final LinkedHashMap<String, Pattern> PATTERNS = new LinkedHashMap<String, Pattern>(
        Map.of("key", KEY_REG, "string", STRING_REG, "boolean", BOOLEAN_REG, "number", NUMBER_REG)
    );

    protected ScrollPane scrollPane;
    protected TextFlow textFlow;
    protected TextArea textArea;

    public Highlighter(
        TextFlow textFlow,
        TextArea textArea,
        ScrollPane scrollPane
    ) {
        this.textArea = textArea;
        this.textFlow = textFlow;
        this.scrollPane = scrollPane;
        this.computeTextFlow(this.textArea.getText());
        this.textArea.setSkin(new CustomTextAreaSkin(this.textArea));
        this.textFlow.widthProperty().addListener((event, old, current) -> this.computeWidth());
        this.textFlow.heightProperty().addListener((event, old, current) -> this.computeHeight());
        this.textArea.scrollLeftProperty().addListener((event, old, current) -> this.computeWidth(current.doubleValue()));
        this.textArea.scrollTopProperty().addListener((event, old, current) -> this.computeHeight(current.doubleValue()));
        this.textFlow.setLineSpacing(1.05078125d);
        this.textArea.textProperty().addListener((event, old, current) -> this.computeTextFlow(current));
    };

     public void setText(String text) {
        this.textArea.setText(text);
    };

    private void computeWidth(double scrollLeft) {
        boolean verticalScrollIsVisible = scrollPane.getContent().getBoundsInLocal().getHeight() > scrollPane.getViewportBounds().getHeight();
        double diff = Math.max(1, this.textFlow.getWidth() - this.textArea.getWidth() - 2d);
        if(verticalScrollIsVisible) diff += 16d;
        this.scrollPane.setHvalue(scrollLeft / diff);
        //System.out.println(scrollLeft + ":" + (scrollLeft / diff) + " : " + verticalScrollIsVisible + " : " + diff + " : " + this.textFlow.getWidth() + " : " + this.textArea.getWidth());
    };

    private void computeWidth() {
        computeWidth(this.textArea.getScrollLeft());
    };

    private void computeHeight(double scrollTop) {
        boolean horizontalScrollIsVisible = scrollPane.getContent().getBoundsInLocal().getWidth() > scrollPane.getViewportBounds().getWidth();
        double diff = Math.max(1, this.textFlow.getHeight() - this.textArea.getHeight() - 2d);
        if(horizontalScrollIsVisible) diff += 20d;
        this.scrollPane.setVvalue(scrollTop / diff);
    };

    private void computeHeight() {
        computeHeight(this.textArea.getScrollTop());
    };

    private void computeTextFlow(String content) {
        this.textFlow.getChildren().clear();
        this.textFlow.getChildren().addAll(this.computeTexts(content));
    };

    protected abstract Text[] computeTexts(String line);
};
