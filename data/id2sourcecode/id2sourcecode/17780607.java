    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").tabs({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, Tabs.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.flush();
        writer.startElement("div", this);
        writer.writeAttribute("id", clientId, "id");
        writer.startElement("ul", this);
        for (UIComponent c : (List<UIComponent>) getChildren()) {
            TabItem t = (TabItem) c;
            writer.startElement("li", this);
            writer.startElement("a", this);
            String url = t.getUrl();
            if (url != null) {
                writer.writeAttribute("href", url, url);
            } else {
                writer.writeAttribute("href", "#" + c.getId(), "#" + c.getId());
            }
            writer.writeText(c.getId(), "childTab");
            writer.endElement("a");
            writer.endElement("li");
        }
        writer.endElement("ul");
        writer.flush();
    }
