    public void testBadURLConnection() throws IOException {
        String link;
        URL url;
        link = "http://www.bigbogosity.org/";
        url = new URL(link);
        try {
            new Page(url.openConnection());
        } catch (ParserException pe) {
        }
    }
