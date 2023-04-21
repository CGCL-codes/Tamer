    public void writePayloadTo(XMLStreamWriter writer) throws XMLStreamException {
        if (payloadLocalName == null) return;
        assert unconsumed();
        XMLStreamReaderToXMLStreamWriter conv = new XMLStreamReaderToXMLStreamWriter();
        while (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
            String name = reader.getLocalName();
            String nsUri = reader.getNamespaceURI();
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                if (!name.equals("Body") || !nsUri.equals(soapVersion.nsUri)) {
                    XMLStreamReaderUtil.nextElementContent(reader);
                    if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) break;
                    name = reader.getLocalName();
                    nsUri = reader.getNamespaceURI();
                }
            }
            if (name.equals("Body") && nsUri.equals(soapVersion.nsUri) || (reader.getEventType() == XMLStreamConstants.END_DOCUMENT)) break;
            conv.bridge(reader, writer);
        }
        XMLStreamReaderUtil.readRest(reader);
        XMLStreamReaderUtil.close(reader);
        XMLStreamReaderFactory.recycle(reader);
    }
