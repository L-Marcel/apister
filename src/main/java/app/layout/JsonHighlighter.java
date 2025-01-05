package app.layout;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class JsonHighlighter extends Highlighter {
    public JsonHighlighter(
        TextFlow textFlow,
        TextArea textArea,
        ScrollPane scrollPane
    ) {
        super(
            textFlow, 
            textArea, 
            scrollPane
        );
    };

    @Override
    protected LinkedHashMap<String, Pattern> initPatterns() {
        LinkedHashMap<String, Pattern> patterns = new LinkedHashMap<String, Pattern>();
        patterns.put("key", JSON_KEY_REG);
        patterns.put("string", STRING_REG);
        patterns.put("boolean", BOOLEAN_REG);
        patterns.put("number", NUMBER_REG);

        return patterns;
    };

    @Override
    protected Text[] computeTexts(String line) {
        return this.defaultComputeTexts(line);
    };
};
