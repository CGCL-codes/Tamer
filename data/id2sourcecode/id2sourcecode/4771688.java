    public static String guess(URL url) throws IOException {
        InputStream in = url.openStream();
        return guess(in);
    }
