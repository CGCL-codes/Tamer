    protected Reader getText() throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }
