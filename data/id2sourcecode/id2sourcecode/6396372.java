    public static String readURL(URL url) throws IOException {
        return readStream(url.openStream());
    }
