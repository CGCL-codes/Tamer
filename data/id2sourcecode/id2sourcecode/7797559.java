    public Collection<DocumentSet> getDocumentSets(String uri) throws ImportException {
        URL url;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new ImportException("Failed to parse URI", e);
        }
        XMLReader xmlReader;
        try {
            xmlReader = factory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            throw new ImportException("Failed to create parser OPML", e);
        }
        FetchingHandler handler = new FetchingHandler();
        xmlReader.setContentHandler(handler);
        InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            throw new ImportException("Failed to read OPML", e);
        }
        try {
            xmlReader.parse(new InputSource(stream));
        } catch (Exception e) {
            throw new ImportException("Failed to parse OPML", e);
        }
        Collection<DocumentSet> sets = new ArrayList<DocumentSet>();
        for (String feedUrl : handler.getUrls()) {
            sets.addAll(feedSource.getDocumentSets(feedUrl));
        }
        return sets;
    }
