package org.gruposp2p.aula.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestletGWT implements EntryPoint {

    /**
	   * The current style theme.
	   */
    public static String CUR_THEME = AulaConstants.STYLE_THEMES[0];

    /**
   * The {@link Application}.
   */
    private AulaApplication app = new AulaApplication();

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        RootPanel.getBodyElement().getStyle().setProperty("display", "none");
        RootPanel.getBodyElement().getStyle().setProperty("display", "");
        RootPanel.get().add(app);
        StyleSheetLoader.loadStyleSheet("Aula.css");
        StyleSheetLoader.loadStyleSheet(GWT.getModuleBaseURL() + "Aula.css");
    }
}
