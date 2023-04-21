    public void load(String name) throws IOException {
        ClassLoader loader = getClass().getClassLoader();
        _url = loader.getResource(name + ".properties");
        if (_url != null) {
            InputStream in = _url.openStream();
            InputStreamReader reader = new InputStreamReader(in);
            load(reader);
        }
    }
