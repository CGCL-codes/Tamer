    private void renderSelectHead(final ResponseWriter writer, final String untergruppenBoxId, final boolean readonly) throws IOException {
        writer.startElement("select", this);
        writer.writeAttribute("style", "width: 300px;", null);
        writer.writeAttribute("id", untergruppenBoxId, null);
        if (readonly) {
            writer.writeAttribute("disabled", "true", null);
            writer.writeAttribute("class", "readonly", null);
        }
    }
