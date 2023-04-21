    private static Document getDocument(Source source) throws SAXException, IOException {
        if (source instanceof DOMSource) {
            Node node = ((DOMSource) source).getNode();
            if (node != null && node instanceof Document) return (Document) node;
        }
        String url = source.getSystemId();
        try {
            InputSource input = new InputSource(url);
            if (source instanceof StreamSource) {
                StreamSource streamSource = (StreamSource) source;
                input.setByteStream(streamSource.getInputStream());
                input.setCharacterStream(streamSource.getReader());
            }
            if (input.getByteStream() == null && input.getCharacterStream() == null && url != null) input.setByteStream(new URL(url).openStream());
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setNamespaceAware(true);
            f.setCoalescing(true);
            f.setExpandEntityReferences(true);
            f.setIgnoringComments(true);
            f.setIgnoringElementContentWhitespace(true);
            DocumentBuilder b = f.newDocumentBuilder();
            return b.parse(input);
        } catch (ParserConfigurationException e) {
            SAXException e2 = new SAXException(e.getMessage());
            e2.initCause(e);
            throw e2;
        }
    }
