    public static Configuration loadConf(URL url) {
        XStream stream = new XStream(new DomDriver());
        InputStream input;
        try {
            input = url.openStream();
            stream.registerConverter(new ConfigurationXmlConverter());
            stream.registerConverter(new LocalDiskStoreXmlConverter());
            stream.registerConverter(new PathXmlConverter());
            stream.alias(CONFIGURATION, Configuration.class);
            stream.alias(LOCAL_DISK_STORE, LocalSystemFileStorage.class);
            stream.alias(PATH, Path.class);
            Configuration conf = (Configuration) stream.fromXML(input);
            return conf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
