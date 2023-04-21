    public static InputStream getURLStream(String path) throws NoSuchPathException, IOException {
        URL url = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            throw new NoSuchPathException(e.getMessage());
        }
        if (url != null) {
            return url.openStream();
        } else {
            return null;
        }
    }
