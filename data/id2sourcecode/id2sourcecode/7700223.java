    public void testURLConnection() throws ParserException, IOException {
        String link;
        URL url;
        link = "http://www.ibm.com/jp/";
        url = new URL(link);
        new Page(url.openConnection());
    }
