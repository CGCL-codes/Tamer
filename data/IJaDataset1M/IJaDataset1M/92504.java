package org.objetdirect.wickext.ui.accordion;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.objetdirect.wickext.core.commons.WickextPlugin;
import org.objetdirect.wickext.core.javascript.JsQuery;
import org.objetdirect.wickext.core.javascript.JsStatement;
import org.objetdirect.wickext.core.options.Options;
import org.objetdirect.wickext.ui.commons.WickextUIPlugin;

/**
 * $Id: Accordion.java 61 2008-11-16 20:10:08Z lionel.armanet $
 * <p>
 * Creates an accordion UI component from this {@link WebMarkupContainer}'s
 * HTML markup.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.6
 */
@WickextUIPlugin
public class Accordion extends WebMarkupContainer implements WickextPlugin {

    private static final long serialVersionUID = 1L;

    /**
	 * $Id: Accordion.java 61 2008-11-16 20:10:08Z lionel.armanet $
	 * <p>
	 * Defines the possible events to trigger the accordion's content switching.
	 * </p>
	 * 
	 * @author Lionel Armanet
	 * @since 0.6
	 */
    public static enum AccordionTriggerEvent {

        CLICK, MOUSEOVER
    }

    /**
	 * The options set for this component.
	 */
    private Options options;

    public Accordion(String id) {
        super(id);
        options = new Options();
    }

    public IHeaderContributor getHeaderContribution() {
        return new IHeaderContributor() {

            private static final long serialVersionUID = -6585283714994481625L;

            public void renderHead(IHeaderResponse response) {
                response.renderJavascriptReference(new JavascriptResourceReference(Accordion.class, "ui.accordion.js"));
            }
        };
    }

    public JsStatement statement() {
        return new JsQuery(this).$().chain("accordion", options.getJavaScriptOptions());
    }

    /**
	 * Sets if this accordion must always has one opened content.
	 */
    public void setAlwaysOpen(boolean alwaysOpen) {
        this.options.put("alwaysOpen", alwaysOpen);
    }

    /**
	 * Returns true if this accordion must always has an opened content, false
	 * otherwise.
	 */
    public boolean isAlwaysOpen() {
        return this.options.getBoolean("alwaysOpen");
    }

    /**
	 * Sets the effect to apply when the accordion's content is switched.
	 * 
	 * @param animationEffect
	 *            the effect name to apply. Set it empty if you don't want to
	 *            apply any effect.
	 */
    public void setAnimationEffect(String animationEffect) {
        this.options.putLiteral("animated", animationEffect);
    }

    /**
	 * Sets if the accordion's height is fixed to the highest content part.
	 * 
	 * @param autoHeight
	 *            true if this accordion's height is the highest content's one.
	 */
    public void setAutoHeight(boolean autoHeight) {
        this.options.put("autoHeight", autoHeight);
    }

    /**
	 * Returns true if this accordion is auto height.
	 * 
	 * @see #setAutoHeight(boolean)
	 */
    public boolean isAutoHeight() {
        return this.options.getBoolean("autoHeight");
    }

    /**
	 * Sets the {@link AccordionTriggerEvent} to use to open content.
	 */
    public void setTriggerEvent(AccordionTriggerEvent accordionTriggerEvent) {
        this.options.putLiteral("event", accordionTriggerEvent.name().toLowerCase());
    }

    /**
	 * Returns the {@link AccordionTriggerEvent}.
	 * 
	 * @see #setTriggerEvent(org.objetdirect.wickext.ui.accordion.Accordion.AccordionTriggerEvent)
	 */
    public AccordionTriggerEvent getTriggerEvent() {
        String literal = this.options.getLiteral("event");
        return AccordionTriggerEvent.valueOf(literal.toUpperCase());
    }

    /**
	 * Makes this accordion's height to the maximum size possible in this'
	 * parent container.
	 * <p>
	 * <em>Overrides {@link #setAutoHeight(boolean)} behavior</em>
	 * </p>
	 */
    public void setFillSpace(boolean fillSpace) {
        this.options.put("fillSpace", fillSpace);
    }

    /**
	 * Returns if this accordion fill space.
	 * 
	 * @see #setFillSpace(boolean)
	 */
    public boolean getFillSpace() {
        return this.options.getBoolean("fillSpace");
    }

    /**
	 * Sets the CSS selector used to defined a header in this accordion.
	 */
    public void setHeader(String headerSelector) {
        this.options.putLiteral("header", headerSelector);
    }
}
