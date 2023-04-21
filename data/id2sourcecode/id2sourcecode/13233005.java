    private Element fetchFromURL(String url_string) throws MalformedURLException, IOException, JDOMException {
        URL url = new URL(url_string);
        InputStream is = url.openStream();
        Document d = new SAXBuilder().build(is);
        is.close();
        return d.getRootElement();
    }
