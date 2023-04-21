    public Document getDocument(URL url) throws Exception {
        return getDocument(url.openStream());
    }
