    public static InputStream getResourceStream(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("URL is null");
        }
        try {
            return new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }
