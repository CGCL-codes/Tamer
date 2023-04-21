    public TaggedSource parse(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        return parse(conn.getInputStream());
    }
