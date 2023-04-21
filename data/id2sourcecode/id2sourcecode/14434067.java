    protected Document parsePage(URL url) throws IOException, SAXException {
        CachedPage page = new CachedPage(url) {

            @Override
            protected Reader openConnection(URL url) throws IOException {
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla");
                return getReader(connection);
            }
        };
        return getHtmlDocument(page.get());
    }
