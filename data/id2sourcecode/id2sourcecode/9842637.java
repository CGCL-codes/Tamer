    private Document parseResponse(String url) throws IOException, MalformedURLException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream stream = null;
        try {
            stream = new URL(url).openStream();
            return db.parse(stream);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }
