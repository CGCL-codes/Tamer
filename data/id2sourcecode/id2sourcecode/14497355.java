    public static Reader getReader(URL url) throws IOException {
        return (getReader(url.openStream()));
    }
