    public static URLConnection createConnection(URL url) throws IOException {
        if (url == null) {
            throw new IOException("Invalid URL: url is null.");
        }
        URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("User-Agent", PropertyManager.getUserAgent());
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setFollowRedirects(true);
        }
        return connection;
    }
