    public Document getDocumentForURL(String location) throws MalformedURLException, IOException, SAXException {
        URL url = new URL(location);
        InputStream in = url.openStream();
        return this.getDocumentFromInputStream(in);
    }
