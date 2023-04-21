    public static Document getXHTMLFromURL(final URL url) throws IOException, JDOMException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setInstanceFollowRedirects(true);
        String encoding = con.getContentEncoding();
        if (encoding == null) {
            encoding = "utf-8";
        }
        BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
        Document xhtmlDocument = XMLConverter.cleanHTML(response).getDocument();
        con.disconnect();
        return xhtmlDocument;
    }
