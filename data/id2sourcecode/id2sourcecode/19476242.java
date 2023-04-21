    private Document readDocument(URL url) throws IOException, JDOMException {
        this.url = url;
        Reader r = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
        Document document = new SAXBuilder().build(r);
        return ConfigConverter.getInstance().convert(document);
    }
