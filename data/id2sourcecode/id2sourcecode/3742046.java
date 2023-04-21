    public static InputStream getStream(URL url) throws IOException {
        if (url != null) {
            return url.openStream();
        } else {
            return null;
        }
    }
