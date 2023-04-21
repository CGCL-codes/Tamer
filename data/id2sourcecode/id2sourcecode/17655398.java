    public static URLConnection openConnection(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
