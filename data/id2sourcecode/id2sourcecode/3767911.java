    public static StringBuilder slurp(URL url) throws IOException {
        return slurp(url.openStream());
    }
