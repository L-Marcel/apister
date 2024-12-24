package app.layout;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHighlighter {
    private final Pattern STRING_REG = Pattern.compile("(\\\"([^\\\"]*)\\\"|\\'.\\'|\\'\\')");
    private final Pattern BOOLEAN_REG = Pattern.compile("(true|false)");
    private final Pattern NUMBER_REG = Pattern.compile("(\\d)");

    private final LinkedHashMap<String, Pattern> PATTERNS = new LinkedHashMap<String, Pattern>(
        Map.of("string", STRING_REG, "boolean", BOOLEAN_REG, "number", NUMBER_REG)
    );

    private ScrollPane scrollPane;
    private TextFlow textFlow;
    private TextArea textArea;

    public JsonHighlighter(
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
        this.textFlow.setLineSpacing(18d - this.textFlow.getBaselineOffset());
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

    private Text[] computeTexts(String line) {
        ArrayList<String> keys = new ArrayList<String>(this.PATTERNS.keySet());
        return this.computeTexts(line, keys, 0);
    };

    private Text[] computeTexts(String line, ArrayList<String> keys, int index) {
        LinkedList<Text> texts = new LinkedList<Text>();
        if(line.isEmpty()) return texts.toArray(Text[]::new);
        else if(index >= keys.size()) {
            Text text = new Text(line);
            text.getStyleClass().add("token-none");
            texts.add(text);
            return texts.toArray(Text[]::new);
        };

        String key = keys.get(index);
        Matcher matcher = this.PATTERNS.get(key).matcher(line);
        if(matcher.find()) {
            String before = line.substring(0, matcher.start());
            for(Text beforeText : this.computeTexts(before, keys, index)) texts.add(beforeText);

            String match = matcher.group();

            Text text = new Text(match);
            text.getStyleClass().add("token-" + key);
            texts.add(text);

            String after = line.substring(matcher.end());
            for(Text afterText : this.computeTexts(after, keys, index)) texts.add(afterText);
        } else {
            for(Text text : this.computeTexts(line, keys, ++index)) texts.add(text);
        };

        return texts.toArray(Text[]::new);
    };
};
