    public static InputStream getStream(Class domainAnchor, String fileName) {
        URL url = getURL(domainAnchor, fileName);
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }
