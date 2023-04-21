    public XmlStreamReader(URL url) throws IOException {
        this(url.openConnection(), null);
    }
