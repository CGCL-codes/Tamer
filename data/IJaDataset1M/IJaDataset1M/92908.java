package org.youchan.mashedpotato.framework.ui.swing;

import javax.swing.JComponent;
import javax.swing.JTextField;
import org.youchan.mashedpotato.framework.model.Getter;
import org.youchan.mashedpotato.framework.model.Setter;

public class TextField extends AbstractWidget {

    static {
        initialize(TextField.class);
    }

    JTextField textField;

    @Setter
    public void setText(String text) {
        textField.setText(text);
    }

    @Getter
    public String getText() {
        return textField.getText();
    }

    public void dispose() {
    }

    public void configure() {
    }

    @Override
    protected JComponent createComponent() {
        textField = new JTextField();
        return textField;
    }
}
