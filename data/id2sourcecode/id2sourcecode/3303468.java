    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Map attr = component.getAttributes();
        JSFUtility.renderScriptOnce(writer, component, context);
        writer.startElement(HTMLTAG_SCRIPT, component);
        writer.writeAttribute(HTMLATTR_TYPE, "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = component.getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").accordion({", null);
        boolean comma = JSFUtility.writeJSObjectOptions(writer, attr, AccordionComponent.ATTRNAME_LIST);
        if (comma) {
            writer.write(CHAR_COMMA);
        }
        writer.writeText("\nheader:\"" + HTMLTAG_H3 + "\"", null);
        writer.writeText("});\n" + "});", null);
        writer.endElement(HTMLTAG_SCRIPT);
        writer.startElement(HTMLTAG_DIV, component);
        writer.writeAttribute(HTMLATTR_ID, component.getClientId(context), "Accordion");
        if (attr.get(Style.STYLE) != null) {
            writer.writeAttribute(Style.STYLE, attr.get(Style.STYLE), null);
        }
        if (attr.get(Style.STYLE_CLASS) != null) {
            writer.writeAttribute("class", attr.get(Style.STYLE_CLASS), null);
        }
        writer.flush();
    }
