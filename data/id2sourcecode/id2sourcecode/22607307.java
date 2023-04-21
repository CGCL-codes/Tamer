    private void encodeInputField(UIComponent spinner, ResponseWriter writer, String clientId) throws IOException {
        writer.startElement("input", spinner);
        writer.writeAttribute("name", clientId, "clientId");
        writer.writeAttribute("value", itemList[increment], "value");
        Integer size = (Integer) spinner.getAttributes().get("size");
        if (size != null) {
            writer.writeAttribute("size", size, "size");
        }
        writer.writeAttribute("readonly", Boolean.TRUE, "true");
        writer.endElement("input");
    }
