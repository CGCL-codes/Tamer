    public static byte[] slurp(URL url, int estimate) throws IOException {
        return slurp(url.openStream(), estimate);
    }
