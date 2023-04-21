    public IRuleSet getRuleSetByURL(String url) throws SWRLXParseException, IOException {
        URL url2 = new URL(url);
        InputStream stream = url2.openStream();
        return this.getRuleSetByInputStream(stream);
    }
