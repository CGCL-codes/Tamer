    public XmlComponentLoader(URL url) throws IOException {
        this.xmlStream = url.openStream();
        this.streamExternal = false;
    }
