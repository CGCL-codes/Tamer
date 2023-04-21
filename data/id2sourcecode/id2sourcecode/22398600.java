    protected void parseRootElement() throws DataDirectException {
        try {
            writer.writeStartDocument();
            writer.writeCharacters(Parser.END_OF_LINE);
            writer.writeStartElement("xtvd");
            try {
                writer.writeAttribute("from", (new DateTime(reader.getAttributeValue(null, "from"))).toString());
                writer.writeAttribute("to", (new DateTime(reader.getAttributeValue(null, "to"))).toString());
                writer.writeAttribute("xmlns", reader.getNamespaceURI(0));
                writer.writeAttribute("xmlns", reader.getNamespaceURI(0), "xsi", reader.getNamespaceURI(1));
                writer.writeAttribute("xsi", reader.getNamespaceURI(1), "schemaLocation", reader.getAttributeValue(null, "schemaLocation"));
            } catch (java.text.ParseException pex) {
                pex.printStackTrace();
            }
            writer.writeAttribute("schemaVersion", reader.getAttributeValue(null, "schemaVersion"));
            writer.writeCharacters(Parser.END_OF_LINE);
        } catch (XMLStreamException xsex) {
            throw new DataDirectException(xsex.getMessage(), xsex);
        }
    }
