    private Element getXML(String url_string) throws MalformedURLException, IOException, JDOMException {
        URL url = new URL(url_string.trim().replaceAll("&amp;", "&"));
        System.err.println(url.toString());
        InputStream is = url.openStream();
        Element e = new SAXBuilder().build(is).getRootElement();
        is.close();
        return e;
    }
