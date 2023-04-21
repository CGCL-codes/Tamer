    protected Reader getReader(URL url) throws IOException {
        return new InputStreamReader(url.openConnection().getInputStream());
    }
