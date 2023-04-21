    public static InputStream getResourceAsStream(String resourceName) throws FileNotFoundException {
        URL url = getURL(resourceName);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }
