    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/menu/jquery-ui-menu.js" };
        String[] cssfiles = { "plugins/menu/css/jquery-ui-menu.css", "plugins/menu/css/jquery-ui-themeroller.css" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, cssfiles, REQUEST_MAP_MENU);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").menu({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, Menu.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), "Menu");
        writer.startElement("ul", this);
        writer.flush();
    }
