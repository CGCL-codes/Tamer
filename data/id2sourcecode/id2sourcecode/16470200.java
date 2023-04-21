    public static String readFully(URL url) throws IOException {
        return readFully(url.openStream());
    }
