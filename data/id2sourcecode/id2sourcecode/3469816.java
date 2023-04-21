    @Override
    public DOMDocument<Document> getDocument(URL url) throws DocumentReaderException {
        Tidy tidy = new Tidy();
        setupTidy(tidy);
        try {
            org.w3c.dom.Document document = tidy.parseDOM(url.openStream(), null);
            if (tidy.getParseErrors() > 0) {
                throw new DocumentReaderException("JDAS invalid XML: error while parsing");
            }
            return new DOMDocumentDom4jImpl(new DOMReader().read(document));
        } catch (IOException ioEx) {
            throw new DocumentReaderException(ioEx.getCause());
        }
    }
