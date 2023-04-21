    public static void reader2writer(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        builder.releaseParserOnClose(true);
        try {
            OMDocument omDocument = builder.getDocument();
            Iterator it = omDocument.getChildren();
            while (it.hasNext()) {
                OMNode omNode = (OMNode) it.next();
                omNode.serializeAndConsume(writer);
            }
        } finally {
            builder.close();
        }
    }
