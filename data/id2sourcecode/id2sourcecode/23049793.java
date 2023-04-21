    @Override
    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (localName.equals("tr")) this.rowLevel++;
        if (rowIsATemplate) return true;
        if (localName.equals("tbl")) {
            depth++;
        } else if (localName.equals("tr") && rowLevel == 1) {
            onMemoryWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(onMemoryStream, "UTF-8");
            this.writer = onMemoryWriter;
            this.underlayingOutputStream = onMemoryStream;
            this.writer.setNamespaceContext(reader.getNamespaceContext());
        } else if (localName.equals("fldChar")) {
            String fldCharType = attributes.getValue("w:fldCharType");
            rowIsATemplate = fldCharType.equals("end");
        } else if (localName.equals("fldSimple")) {
            rowIsATemplate = true;
        }
        return false;
    }
