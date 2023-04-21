    public static Document getWebDoc(String prot, String host, int port, String path) {
        URL url_to_connect;
        InputStream stream;
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            url_to_connect = new URL(prot, host, port, path);
            stream = url_to_connect.openStream();
            doc = db.parse(stream);
        } catch (Exception e) {
            log.error(e);
        }
        return doc;
    }
