    public static Properties getGameSettings() {
        Properties props, defaults = new Properties();
        try {
            URL url = res.getClass().getResource("system.properties");
            if (url == null) defaults = null; else {
                InputStream in = url.openStream();
                defaults.load(in);
                in.close();
            }
        } catch (IOException ioe) {
            defaults = null;
        }
        props = new Properties(defaults);
        res.ensureStoreFolder();
        try {
            URL url = STORE.toURL();
            InputStream in = new URL(url.toString() + "/system.properties").openStream();
            props.load(in);
            in.close();
        } catch (IOException ioe) {
            return defaults;
        }
        return props;
    }
