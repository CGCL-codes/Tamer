    public static NamedInputStream getInputStream(URL url) throws IOException {
        return new NamedInputStream(url.toString(), url.openStream(), LOOKAHEAD);
    }
