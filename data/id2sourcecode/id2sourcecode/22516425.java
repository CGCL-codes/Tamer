    public static String loadTextFile(URL url) throws IOException {
        return loadTextFile(url.openStream());
    }
