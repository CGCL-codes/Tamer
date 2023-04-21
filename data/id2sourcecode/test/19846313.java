    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/fileupload/jquery.MultiFile.js" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, null, REQUEST_MAP_FILEUPLOADER);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").MultiFile({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, FileUploader.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context) + "_wrap", null);
        writer.startElement("input", this);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("type", "file", null);
        writer.writeAttribute("name", "", null);
        if (attr.get(MAXLENGTH) != null) {
            writer.writeAttribute("maxlength", attr.get(MAXLENGTH), null);
        }
        if (attr.get(LIST) != null) {
            writer.writeAttribute("list", attr.get(MAXLENGTH), null);
        }
        if (attr.get(ACCEPT) != null) {
            writer.writeAttribute("accept", attr.get(ACCEPT), null);
        }
        if (attr.get(CLASS) != null) {
            writer.writeAttribute("class", attr.get(CLASS), null);
        }
        writer.endElement("input");
    }
