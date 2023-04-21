    public void init(String name) {
        Properties p = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URL url = classLoader.getResource(name + ".properties");
            if (url != null) {
                InputStream is = url.openStream();
                p.load(is);
                is.close();
                Logger.info(this, "Loading " + url);
            }
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
        try {
            URL url = classLoader.getResource(name + "-ext.properties");
            if (url != null) {
                InputStream is = url.openStream();
                p.load(is);
                is.close();
                Logger.info(this, "Loading " + url);
            }
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
        PropertiesUtil.fromProperties(p, _props);
    }
