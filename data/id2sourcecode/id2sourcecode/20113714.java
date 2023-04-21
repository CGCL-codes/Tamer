    public static Document readDocument(URL url) throws IOException, JDOMException {
        Reader r = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
        Document document = new SAXBuilder().build(r);
        return document;
    }
