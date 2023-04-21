package org.kablink.teaming.gwt.client.mainmenu;

import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.GwtTeamingMainMenuImageBundle;
import org.kablink.teaming.gwt.client.GwtTeamingMessages;
import org.kablink.teaming.gwt.client.event.SearchSimpleEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Class used for the search widgets on the main menu bar.  
 * 
 * @author drfoster@novell.com
 */
public class SearchMenuPanel extends FlowPanel {

    private GwtTeamingMainMenuImageBundle m_images;

    private GwtTeamingMessages m_messages;

    private TextBox m_searchInput;

    /**
	 * Class constructor.
	 */
    public SearchMenuPanel() {
        super();
        m_images = GwtTeaming.getMainMenuImageBundle();
        m_messages = GwtTeaming.getMessages();
        addStyleName("vibe-mainMenuBar_BoxPanel vibe-mainMenuSearch_Panel");
        addSearchWidget();
        addSearchButton();
    }

    private void addSearchButton() {
        final SearchMenuPanel searchMenu = this;
        Image img = new Image(m_images.searchGlass());
        img.setTitle(m_messages.mainMenuSearchButtonAlt());
        img.addStyleName("vibe-mainMenuSearch_ButtonImage");
        Anchor searchAnchor = new Anchor();
        searchAnchor.getElement().appendChild(img.getElement());
        searchAnchor.addStyleName("vibe-mainMenuSearch_ButtonAnchor");
        searchAnchor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                searchMenu.getElement().removeClassName("subhead-control-bg2");
                doSearch();
            }
        });
        MenuHoverByWidget hover = new MenuHoverByWidget(searchMenu, "subhead-control-bg2");
        searchAnchor.addMouseOverHandler(hover);
        searchAnchor.addMouseOutHandler(hover);
        add(searchAnchor);
    }

    private void addSearchWidget() {
        m_searchInput = new TextBox();
        m_searchInput.addStyleName("vibe-mainMenuSearch_Input");
        m_searchInput.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                int key = event.getNativeEvent().getKeyCode();
                if (KeyCodes.KEY_ENTER == key) {
                    doSearch();
                }
            }
        });
        add(m_searchInput);
    }

    private void doSearch() {
        String searchFor = m_searchInput.getValue();
        m_searchInput.setValue("");
        GwtTeaming.fireEvent(new SearchSimpleEvent(searchFor));
    }
}
