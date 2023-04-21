    private static synchronized URLConnection getConnection(final URL url) {
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            connection.setConnectTimeout(getConnectionTimeout() * 1000);
            connection.setReadTimeout(getConnectionTimeout() * 1000);
        }
        return connection;
    }
