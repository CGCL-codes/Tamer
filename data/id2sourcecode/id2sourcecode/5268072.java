    public void readHTableRDF(URL url) throws Exception {
        this.readTableRDF(url.openStream(), Rio.getParserFormatForFileName(url.getPath()));
    }
