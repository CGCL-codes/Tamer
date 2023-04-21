    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").slider({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, Slider.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), null);
        if (attr.get(STYLE) != null) {
            writer.writeAttribute("style", attr.get(STYLE), STYLE);
        }
        if (attr.get(STYLECLASS) != null) {
            writer.writeAttribute("class", attr.get(STYLECLASS), STYLECLASS);
        }
        writer.flush();
    }
