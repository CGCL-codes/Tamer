    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/layout/jquery.layout.js" };
        String[] cssfiles = { "plugins/layout/css/layout-default.css" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, cssfiles, REQUEST_MAP_BORDERLAYOUT);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(document).ready(function(){\n$(\"body\").layout({\n" + "				applyDefaultStyles: true\n" + "				//,center : {\n" + "				//	paneSelector : '#" + clientId + "'\n" + "				//}\n" + "			});", null);
        writer.writeText("window[\"" + getId() + "\"] = $(\"#" + clientId + "\").layout({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, BorderLayout.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), null);
        String style = (String) attr.get(STYLE);
        if (null == style || style.trim().equals("")) {
            style = "height:100%;position:relative;overflow:hidden;";
        } else if (style.endsWith(";")) {
            style += "height:100%;position:relative;overflow:hidden;";
        } else {
            style += ";height:100%;position:relative;overflow:hidden;";
        }
        writer.writeAttribute("style", style, null);
        if (this.getAttributes().get(STYLECLASS) != null) {
            writer.writeAttribute("class", getAttributes().get(STYLECLASS), null);
        }
        writer.flush();
    }
