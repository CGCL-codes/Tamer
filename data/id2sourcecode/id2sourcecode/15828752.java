    public XmlReader(URL url) throws IOException {
        this(url.openConnection());
    }
