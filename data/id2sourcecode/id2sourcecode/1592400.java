    private void writeUserFieldGet(String sElementLocalName, String sFieldName) throws XMLStreamException {
        writer.writeStartElement(reader.getPrefix(), sElementLocalName, reader.getNamespaceURI());
        int attrbCount = reader.getAttributeCount();
        for (int i = 0; i < attrbCount; i++) {
            String namespaceURI = reader.getAttributeNamespace(i);
            String namespacePrefix = reader.getAttributePrefix(i);
            String attrbLocalName = reader.getAttributeLocalName(i);
            String value;
            if (attrbLocalName.equals("name")) value = sFieldName; else value = reader.getAttributeValue(i);
            if (namespaceURI == null && (namespacePrefix == null || namespacePrefix.length() == 0)) writer.writeAttribute(attrbLocalName, value); else if (namespacePrefix == null || namespacePrefix.length() == 0) writer.writeAttribute(namespaceURI, attrbLocalName, value); else writer.writeAttribute(namespacePrefix, namespaceURI, attrbLocalName, value);
        }
    }
