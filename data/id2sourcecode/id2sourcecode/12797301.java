    public LuaReader(URL url) throws IOException {
        this(createReader(url.openStream()));
    }
