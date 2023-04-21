    WebURL(String url) throws IOException {
        uri = new URL(url);
        urlconn = uri.openConnection();
    }
