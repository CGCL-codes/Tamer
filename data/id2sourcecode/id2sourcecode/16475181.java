    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        writer.writeText("$(\"#" + this.getId() + "\").dialog({", null);
        Map attr = this.getAttributes();
        boolean commaNeeded = JSFUtility.writeJSObjectOptions(writer, attr, Dialog.class);
        if (attr.get(BUTTONS) != null) {
            if (commaNeeded) {
                writer.write(",");
            }
            writer.write("buttons : {");
            writer.write((String) attr.get(BUTTONS));
            writer.write("}");
        }
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getId(), "Dialog");
        if (attr.get(TITLE) != null) {
            writer.writeAttribute("title", (String) attr.get(TITLE), "Dialog");
        }
        writer.flush();
    }
