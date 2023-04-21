    public ScriptIterator(URL url) throws IOException {
        this(new BufferedReader(new InputStreamReader(url.openStream())));
    }
