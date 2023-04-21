    public static InputStream openStream(final URL url) {
        try {
            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (Exception e) {
        }
        return null;
    }
