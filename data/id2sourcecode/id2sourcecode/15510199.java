    protected static URLConnection openConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla");
        connection.connect();
        return connection;
    }
