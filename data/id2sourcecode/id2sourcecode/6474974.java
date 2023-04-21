    private static InputStream _getInputStream(URL url) {
        try {
            return url.openStream();
        } catch (Throwable t) {
            return null;
        }
    }
