    private static long getLength(URL url) {
        URLConnection openConnection = null;
        try {
            openConnection = url.openConnection();
            return openConnection.getContentLength();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
