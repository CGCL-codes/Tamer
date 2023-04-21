    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        Reader reader = new InputStreamReader(url.openStream());
        DocumentSummary docSummary = new DocumentSummary();
        docSummary.contentReader = reader;
        return docSummary;
    }
