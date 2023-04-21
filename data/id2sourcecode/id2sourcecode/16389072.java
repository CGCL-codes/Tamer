    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/fckeditor/jquery.MetaData.js", "plugins/fckeditor/jquery.form.js", "plugins/fckeditor/jquery.FCKEditor.js" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, null, REQUEST_MAP_FCK);
        Map requestMap = context.getExternalContext().getRequestMap();
        Integer fckCounter = (Integer) requestMap.get(REQUEST_MAP_FCK_COUNT);
        if (fckCounter == null) {
            fckCounter = new Integer(0);
        } else {
            fckCounter++;
        }
        requestMap.put(REQUEST_MAP_FCK_COUNT, fckCounter);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        Map attr = this.getAttributes();
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(document).ready(function(){\n", null);
        writer.writeText("$(\"#", null);
        writer.writeText(clientId, null);
        writer.writeText("\").fck({", null);
        boolean commaNeeded = JSFUtility.writeJSObjectOptions(writer, attr, RichTextEditor.class);
        if (commaNeeded == true) {
            writer.writeText(",", null);
        }
        writer.writeText("waitFor: 0, path: 'plugins/fckeditor/'}); });", null);
        writer.endElement("script");
        writer.startElement("textarea", this);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("name", getClientId(context), null);
        if (getStyle() != null) {
            writer.writeAttribute("style", getStyle(), null);
        }
        if (getStyleClass() != null) {
            writer.writeAttribute("class", getStyleClass() + " jsfqueryfck" + fckCounter, null);
        } else {
            writer.writeAttribute("class", "jsfqueryfck" + fckCounter, null);
        }
        if (getRows() != null) {
            writer.writeAttribute("rows", getRows(), null);
        }
        if (getCols() != null) {
            writer.writeAttribute("cols", getCols(), null);
        }
        if (super.getValue() != null) {
            writer.writeText(super.getValue(), null);
        }
        writer.endElement("textarea");
        writer.flush();
    }
