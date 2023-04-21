    protected void readConfiguration() throws Exception {
        Properties props = new Properties();
        Enumeration<URL> modules = getClassLoader().getResources(PLUGINS);
        while (modules.hasMoreElements()) {
            URL url = (URL) modules.nextElement();
            props.load(url.openStream());
        }
        registerFeatures(props);
        props.clear();
        InputStream config = getClassLoader().getResourceAsStream(DEFAULT);
        if (config != null) {
            props.load(config);
        }
        config = getClassLoader().getResourceAsStream(CONFIG);
        if (config != null) {
            props.load(config);
        }
        readFeatureGroupsConfiguration(props);
    }
