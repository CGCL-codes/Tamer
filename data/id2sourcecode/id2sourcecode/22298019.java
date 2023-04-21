    private static void load(URL url) throws IOException {
        Properties props = CollectionUtil.readProperties(url.openStream(), null);
        for (String name : props.stringPropertyNames()) map.put(Level.parse(name), new Ansi(props.getProperty(name)));
    }
