    public static void register(URL codeBase) throws Exception {
        Properties properties = new Properties();
        try {
            URL url = new URL(codeBase + CONFIG_FILE_PATH);
            properties.load(url.openStream());
            register(properties);
        } catch (Exception e) {
            log.fatal(e);
            throw e;
        }
    }
