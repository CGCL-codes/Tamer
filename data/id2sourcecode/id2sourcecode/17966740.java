    private Element getPolicy(String href) throws IOException, SAXException, ParserConfigurationException {
        InputSource source = null;
        if (href != null && baseUrl != null) {
            URL url;
            try {
                url = new URL(baseUrl, href);
                source = new InputSource(url.openStream());
                source.setSystemId(href);
            } catch (MalformedURLException except) {
                try {
                    String absURL = URIUtils.resolveAsString(href, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(href);
                } catch (MalformedURLException ex2) {
                }
            } catch (java.io.FileNotFoundException fnfe) {
                try {
                    String absURL = URIUtils.resolveAsString(href, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(href);
                } catch (MalformedURLException ex2) {
                }
            }
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = null;
        if (source != null) {
            dom = db.parse(source);
            Element topLevelElement = dom.getDocumentElement();
            return topLevelElement;
        }
        return null;
    }
