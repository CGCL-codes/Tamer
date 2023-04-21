    public static byte[] slurp(URL url, int estimate, int max) throws IOException {
        return slurp(url.openStream(), estimate, max);
    }
