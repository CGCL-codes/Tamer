    public static XTree readFromURL(URL url) throws Exception {
        return readFromStream(url.openStream());
    }
