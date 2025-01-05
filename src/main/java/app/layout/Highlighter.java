package app.layout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.StringProperty;
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
    protected CustomTextAreaSkin skin;
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
        this.skin = new CustomTextAreaSkin(this.textArea);
        this.textArea.setSkin(this.skin);
        this.textFlow.setTabSize(2);
       
        this.skin.getContent().layoutBoundsProperty().addListener((event, old, current) -> {
            this.computeHeight(current.getHeight());
            this.computeWidth(current.getWidth());
        });

        this.scrollPane.hvalueProperty().addListener((event, old, current) -> this.computeHorizontalScroll(current.doubleValue(), this.textArea.getScrollLeft()));
        this.textArea.scrollLeftProperty().addListener((event, old, current) -> this.computeHorizontalScroll(this.scrollPane.getHvalue(), current.doubleValue()));
        this.scrollPane.vvalueProperty().addListener((event, old, current) -> this.computeVerticalScroll(current.doubleValue(), this.textArea.getScrollTop()));
        this.textArea.scrollTopProperty().addListener((event, old, current) -> this.computeVerticalScroll(this.scrollPane.getVvalue(), current.doubleValue()));
       
        this.textArea.textProperty().addListener((event, old, current) -> this.computeTextFlow(current));
        this.computeTextFlow(this.textArea.getText());
    };

    protected abstract LinkedHashMap<String, Pattern> initPatterns();
    protected abstract Text[] computeTexts(String line);

    public void setText(StringProperty text) {
        this.textArea.textProperty().bind(text);
    };

    private void computeHeight(double height) {
        if(height != this.textFlow.getHeight()) {
            this.textFlow.setPrefHeight(height);
            if(height <= this.textArea.getHeight()) this.computeLineSpacing(height, false);
            else this.computeLineSpacing(height);
        };
    };

    private void computeWidth(double width) {
        if(width != this.textFlow.getWidth()) this.textFlow.setPrefWidth(width);
    };

    private void computeLineSpacing(double height) {
        this.computeLineSpacing(height, this.isVerticalScrollVisible());
    };

    private void computeLineSpacing(double height, boolean dynamic) {
        if(dynamic) {
            int lines = 1 + this.textArea.getText().length() - this.textArea.getText().replaceAll("\n", "").length();
            double targetLineHeight = (height - 8d) / lines;
            double diff = ((targetLineHeight - 13.59375d) * lines) / (lines - 1);
            this.textFlow.setLineSpacing(diff);
        } else {
            this.textFlow.setLineSpacing(1.40404d);
        };
    };

    private void computeVerticalScroll(double textFlowScroll, double textAreaScrollTop) {
        double max = this.skin.getContentHeight() - this.textArea.getHeight();
        if(this.isHorizontalScrollVisible()) max += 16d;

        if(max > 0) {
            double target = textAreaScrollTop / max;
            this.scrollPane.setVvalue(target);
        } else if(textFlowScroll != 0) {
            this.scrollPane.setVvalue(0);
        };
    };

    private void computeHorizontalScroll(double textFlowScroll, double textAreaScrollLeft) {
        double max = this.skin.getContentWidth() - this.textArea.getWidth();
        if(this.isVerticalScrollVisible()) max += 16d;

        if(max > 0) {
            double target = textAreaScrollLeft / max;
            this.scrollPane.setHvalue(target);
        } else if(textFlowScroll != 0) {
            this.scrollPane.setHvalue(0);
        };
    };

    private boolean isHorizontalScrollVisible() {
        return this.skin.getTextAreaScrollPane().getContent().getBoundsInLocal().getWidth() > this.skin.getTextAreaScrollPane().getViewportBounds().getWidth();
    };

    private boolean isVerticalScrollVisible() {
        return this.skin.getTextAreaScrollPane().getContent().getBoundsInLocal().getHeight() > this.skin.getTextAreaScrollPane().getViewportBounds().getHeight();
    };

    private void computeTextFlow(String content) {
        this.textFlow.getChildren().clear();
        this.textFlow.getChildren().addAll(this.computeTexts(content));
    };

    protected Text[] defaultComputeTexts(String line) {
        ArrayList<String> keys = new ArrayList<String>(this.patterns.keySet());
        return this.defaultComputeTexts(line, keys, 0);
    };
    
    protected Text[] defaultComputeTexts(String line, ArrayList<String> keys, int index) {
        LinkedList<Text> texts = new LinkedList<Text>();
        if(line.isEmpty()) return texts.toArray(Text[]::new);
        else if(index >= keys.size()) {
            Text text = new Text(line);
            text.getStyleClass().addAll("code-text", "token-none");
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
            text.getStyleClass().addAll("code-text", "token-" + key);
            texts.add(text);

            String after = line.substring(matcher.end());
            for(Text afterText : this.defaultComputeTexts(after, keys, index)) texts.add(afterText);
        } else {
            for(Text text : this.defaultComputeTexts(line, keys, index + 1)) texts.add(text);
        };

        return texts.toArray(Text[]::new);
    };
};
