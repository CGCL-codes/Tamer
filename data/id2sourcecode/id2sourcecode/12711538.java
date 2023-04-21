    protected Document getXmlDocument(String urlString) {
        try {
            URL url = UrlFactory.getUrl(urlString);
            URLConnection c = url.openConnection();
            InputStream is = c.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            UrlFactory.disconnect(c);
            return doc;
        } catch (Exception e) {
            throw new XmlCollectorException(e.getMessage(), e);
        }
    }
