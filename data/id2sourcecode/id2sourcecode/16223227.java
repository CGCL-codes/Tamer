    public static void init() throws ConfigException {
        URL url = ClassLoader.getSystemClassLoader().getResource(_APP_CONFIG_XML);
        InputStream in = null;
        try {
            in = url.openStream();
            configInstance = new ConfigManagerFactory(in);
        } catch (IOException ie) {
            throw new ConfigException("Error retrieving config file: " + StackTraceUtil.getStackTrace(ie));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ie) {
                }
            }
        }
    }
