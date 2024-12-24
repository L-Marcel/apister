package app.layout;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;

public class HeaderHighlighter extends Highlighter {
    public HeaderHighlighter(
        TextFlow textFlow,
        TextArea textArea,
        ScrollPane scrollPane
    ) {
        super(textFlow, textArea, scrollPane);
    };

    @Override
    protected Text[] computeTexts(String line) {
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
