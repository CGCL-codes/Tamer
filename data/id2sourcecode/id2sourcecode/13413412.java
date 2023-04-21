    protected void serializeAttributes(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        int count = reader.getAttributeCount();
        String prefix = null;
        String namespaceName = null;
        String writerPrefix = null;
        for (int i = 0; i < count; i++) {
            prefix = reader.getAttributePrefix(i);
            namespaceName = reader.getAttributeNamespace(i);
            namespaceName = (namespaceName == null) ? "" : namespaceName;
            writerPrefix = writer.getPrefix(namespaceName);
            if (!"".equals(namespaceName)) {
                if (writerPrefix != null && (prefix == null || prefix.equals(""))) {
                    writer.writeAttribute(writerPrefix, namespaceName, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                } else if (prefix != null && !"".equals(prefix) && !prefix.equals(writerPrefix)) {
                    writer.writeNamespace(prefix, namespaceName);
                    writer.writeAttribute(prefix, namespaceName, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                } else {
                    prefix = generateUniquePrefix(writer.getNamespaceContext());
                    writer.writeNamespace(prefix, namespaceName);
                    writer.writeAttribute(prefix, namespaceName, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                }
            } else {
                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            }
        }
    }
