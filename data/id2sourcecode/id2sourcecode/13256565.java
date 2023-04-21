    HttpURLConnection openConnection(String path) throws IOException {
        LOG.finest("OpenConnection: " + path);
        URL url = new URL("http", server, path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        client.setCookies(connection, cookie);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }
