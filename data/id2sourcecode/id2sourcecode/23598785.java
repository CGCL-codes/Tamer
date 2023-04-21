    public TextReplace(URL url, String token) throws IOException {
        this(url.openStream(), token);
    }
