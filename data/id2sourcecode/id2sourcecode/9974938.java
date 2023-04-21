    private List<Page> requestHTTPGet(String url) throws MalformedURLException, IOException, XPathExpressionException, ParserConfigurationException, SAXException, TransformerException {
        List<Page> pages = new ArrayList<Page>();
        _logger.debug("url=" + url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                List<Page> xpages = XMLParser.parsePageXML(inputStream);
                pages.addAll(xpages);
            } else {
                _logger.warn("HTTP Result: " + conn.getResponseMessage());
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return pages;
    }
