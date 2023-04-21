    private IXMLReader getReader(XMLElement element) throws XMLParseException, IOException {
        String href = element.getAttribute(HREF_ATTRIB);
        URL url = null;
        try {
            url = new URL(href);
        } catch (MalformedURLException e) {
            try {
                if (href.charAt(0) == '/') {
                    url = new URL("file://" + href);
                } else {
                    url = new URL(new URL(element.getSystemID()), href);
                }
            } catch (MalformedURLException e1) {
                new XMLParseException(element.getSystemID(), element.getLineNr(), "malformed url '" + href + "'");
            }
        }
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection && element.hasAttribute(ENCODING_ATTRIB)) {
            connection.setRequestProperty("accept", element.getAttribute(ENCODING_ATTRIB));
        }
        InputStream is = connection.getInputStream();
        InputStreamReader reader = null;
        if (element.getAttribute(PARSE_ATTRIB, "xml").equals("text") && element.hasAttribute(ENCODING_ATTRIB)) {
            reader = new InputStreamReader(is, element.getAttribute(ENCODING_ATTRIB, ""));
        } else {
            reader = new InputStreamReader(is);
        }
        IXMLReader ireader = new StdXMLReader(reader);
        ireader.setSystemID(url.toExternalForm());
        return ireader;
    }
