package org.systemsbiology.apps.gui.client.widget.general;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class ToolTip extends PopupPanel {

    private static final String toolTipStyle = "tooltip";

    public ToolTip(String message) {
        this.setStyleName(toolTipStyle);
        add(new Label(message));
    }

    public ToolTip(HTML message) {
        this.setStyleName(toolTipStyle);
        add(message);
    }
}
