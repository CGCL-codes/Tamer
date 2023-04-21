    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        Reader reader = new InputStreamReader(url.openStream());
        BufferedReader br = new BufferedReader(reader);
        CallbackHandler handler = new CallbackHandler();
        new ParserDelegator().parse(br, handler, true);
        DocumentSummary docSummary = new DocumentSummary();
        docSummary.authors = handler.metaAuthors;
        docSummary.contentReader = new StringReader(handler.docTextContent.toString());
        docSummary.keywords = handler.metaKeywords;
        docSummary.modificationDate = handler.metaDate;
        docSummary.summary = getSummary(handler);
        docSummary.title = handler.title;
        return docSummary;
    }
