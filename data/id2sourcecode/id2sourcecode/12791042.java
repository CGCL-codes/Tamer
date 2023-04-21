    public static Document getWebDoc(String url) {
        URL url_to_connect;
        BufferedInputStream stream = null;
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            url_to_connect = new URL(url);
            URLConnection connection = url_to_connect.openConnection();
            stream = new BufferedInputStream(connection.getInputStream());
            doc = db.parse(stream);
        } catch (Exception e) {
            log.error(e + "\n" + url);
            try {
                stream.close();
            } catch (Exception e1) {
            }
        }
        return doc;
    }
