package app.layout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class Highlighter {
    static final Pattern HEADER_KEY_REG = Pattern.compile("^([\\w-]+)(?:)", Pattern.MULTILINE);
    static final Pattern STRING_REG = Pattern.compile("(\\\"([^\\\"]*)\\\"|\\'.\\'|\\'\\')");
    static final Pattern JSON_KEY_REG = Pattern.compile("(\\\"([^\\\"]*)\\\"(?=[ \t]*:))", Pattern.MULTILINE);
    static final Pattern BOOLEAN_REG = Pattern.compile("(true|false)");
    static final Pattern NUMBER_REG = Pattern.compile("(\\d)");

    protected LinkedHashMap<String, Pattern> patterns;
    protected ScrollPane scrollPane;
    protected TextFlow textFlow;
    protected TextArea textArea;

    public Highlighter(
        TextFlow textFlow,
        TextArea textArea,
        ScrollPane scrollPane
    ) {
        this.patterns = initPatterns();
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

    protected abstract LinkedHashMap<String, Pattern> initPatterns();
    protected abstract Text[] computeTexts(String line);
    
    protected Text[] defaultComputeTexts(String line) {
        ArrayList<String> keys = new ArrayList<String>(this.patterns.keySet());
        return this.defaultComputeTexts(line, keys, 0);
    };
    
    protected Text[] defaultComputeTexts(String line, ArrayList<String> keys, int index) {
        LinkedList<Text> texts = new LinkedList<Text>();
        if(line.isEmpty()) return texts.toArray(Text[]::new);
        else if(index >= keys.size()) {
            Text text = new Text(line);
            text.getStyleClass().add("token-none");
            texts.add(text);
            return texts.toArray(Text[]::new);
        };

        String key = keys.get(index);
        Matcher matcher = this.patterns.get(key).matcher(line);
        if(matcher.find()) {
            String before = line.substring(0, matcher.start());
            for(Text beforeText : this.defaultComputeTexts(before, keys, index)) texts.add(beforeText);

            String match = matcher.group();

            Text text = new Text(match);
            text.getStyleClass().add("token-" + key);
            texts.add(text);

            String after = line.substring(matcher.end());
            for(Text afterText : this.defaultComputeTexts(after, keys, index)) texts.add(afterText);
        } else {
            for(Text text : this.defaultComputeTexts(line, keys, index + 1)) texts.add(text);
        };

        return texts.toArray(Text[]::new);
    };
};
