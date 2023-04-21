package org.jquery4jsf.custom.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jquery4jsf.renderkit.HtmlBasicRenderer;
import org.jquery4jsf.renderkit.html.HTML;
import org.jquery4jsf.renderkit.html.HtmlRendererUtilities;

public class HtmlOutputLinkRenderer extends HtmlBasicRenderer {

    protected Object getValue(UIComponent uicomponent) {
        return ((UIOutput) uicomponent).getValue();
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        HtmlOutputLink outputLink = null;
        if (component instanceof HtmlOutputLink) {
            outputLink = (HtmlOutputLink) component;
        }
        if (!outputLink.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.TAG_A, outputLink);
        HtmlRendererUtilities.writeHtmlAttributes(writer, component, HTML.HTML_STD_ATTR);
        HtmlRendererUtilities.writeHtmlAttributes(writer, component, HTML.HTML_JS_STD_ATTR);
        HtmlRendererUtilities.writeHtmlAttributes(writer, component, HTML.HTML_JS_ELEMENT_ATTR);
        HtmlRendererUtilities.writeHtmlAttributes(writer, component, HTML.HTML_A_TAG_ATTR);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        super.encodeChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        HtmlOutputLink outputLink = null;
        if (component instanceof HtmlOutputLink) {
            outputLink = (HtmlOutputLink) component;
        }
        if (!outputLink.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement(HTML.TAG_A);
    }
}
