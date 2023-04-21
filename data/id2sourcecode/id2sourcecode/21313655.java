    public static Document geoParse(String text, String server) throws IOException, MalformedURLException, DocumentException {
        String data = URLEncoder.encode("showmap", "UTF-8") + "=" + URLEncoder.encode("false", "UTF-8");
        data += "&" + URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode(requestStart + EscapeChars.forXML(text) + requestEnd, "UTF-8");
        URL url = new URL(server);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        Document dom = Dom4jUtil.parse(new InputSource(conn.getInputStream()));
        wr.close();
        return dom;
    }
