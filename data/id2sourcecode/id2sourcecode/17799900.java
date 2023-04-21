    private void loadDBConfig(String adapter) throws IOException {
        URL url = getClass().getClassLoader().getResource("adapter/" + adapter + ".properties");
        _props = new Properties();
        _props.load(url.openStream());
        _init = true;
    }
