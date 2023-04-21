    private URLConnection getConnection(String uri) throws IOException {
        URL url = new URL(uri);
        return url.openConnection();
    }
