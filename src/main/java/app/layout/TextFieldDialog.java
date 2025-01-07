package app.layout;

import java.io.IOException;

import app.controllers.components.TextFieldDialogController;
import app.interfaces.Validator;

public class TextFieldDialog extends Dialog<String> {
    public TextFieldDialog(
        String title, 
        String prompt,
        String validMessage,
        Validator<String> validator
    ) throws IOException {
        super(
            title,
            "components/textFieldDialog", 
            new TextFieldDialogController(
                title, 
                prompt, 
                validMessage, 
                "", 
                validator, 
                true
            )
        );
    };

    public TextFieldDialog(
        String title, 
        String prompt,
        String validMessage,
        String initial,
        Validator<String> validator
    ) throws IOException {
        super(
            title,
            "components/textFieldDialog", 
            new TextFieldDialogController(
                title, 
                prompt, 
                validMessage, 
                initial, 
                validator, 
                true
            )
        );
    };
};
