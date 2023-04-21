    public static StringBuffer readFromURL(URL url) {
        if (url == null) {
            log.error("Called readFromURL with null url, returning null.");
            return null;
        }
        try {
            return readFromBufferedReader(new BufferedReader(new InputStreamReader(url.openStream())));
        } catch (IOException e) {
            log.error("readFromURL: IOException opening URL " + url, e);
            return null;
        }
    }
