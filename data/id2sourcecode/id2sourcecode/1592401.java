    private void writeProperty(String sElementLocalName, String sPropertyKey) throws XMLStreamException {
        writer.writeStartElement(reader.getPrefix(), sElementLocalName, reader.getNamespaceURI());
        int attrbCount = reader.getAttributeCount();
        for (int i = 0; i < attrbCount; i++) {
            String namespaceURI = reader.getAttributeNamespace(i);
            String namespacePrefix = reader.getAttributePrefix(i);
            String attrbLocalName = reader.getAttributeLocalName(i);
            String value;
            if (attrbLocalName.equals("string-value")) value = this.model.isPropertyAString(sPropertyKey) ? this.model.getPropertyAsString(sPropertyKey) : String.format("Field %s not found in document.", sPropertyKey); else value = reader.getAttributeValue(i);
            if (namespaceURI == null && (namespacePrefix == null || namespacePrefix.length() == 0)) writer.writeAttribute(attrbLocalName, value); else if (namespacePrefix == null || namespacePrefix.length() == 0) writer.writeAttribute(namespaceURI, attrbLocalName, value); else writer.writeAttribute(namespacePrefix, namespaceURI, attrbLocalName, value);
        }
    }
