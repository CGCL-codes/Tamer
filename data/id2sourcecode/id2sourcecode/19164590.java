    private void renderSelectHead(final ResponseWriter writer, final String rootId, final boolean readonly) throws IOException {
        writer.startElement("select", this);
        writer.writeAttribute("style", "width: 150px;", null);
        writer.writeAttribute("size", "6", null);
        writer.writeAttribute("id", rootId, null);
        if (readonly) {
            writer.writeAttribute("disabled", "true", null);
            writer.writeAttribute("class", "readonly", null);
        }
    }
