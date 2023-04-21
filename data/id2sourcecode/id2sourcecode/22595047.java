    public static InputStream getInputStream(URL url) {
        if (url == null) return null;
        InputStream in = null;
        try {
            in = (url).openStream();
        } catch (java.io.IOException e) {
            System.err.println("ERROR: can't read the file " + url.toString());
        }
        return in;
    }
