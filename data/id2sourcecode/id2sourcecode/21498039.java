    private static URLConnection getConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        return connection;
    }
