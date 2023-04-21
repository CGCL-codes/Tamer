    private InputStream openStreamWithoutCache(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }
