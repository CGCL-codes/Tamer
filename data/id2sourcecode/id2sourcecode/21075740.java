    public static Properties getDefaultDriverDefinitions() {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            URL url = IsqlToolkit.class.getResource("/org/isqlviewer/resource/".concat(DRIVER_DEFINITIONS_FILE));
            inputStream = url.openStream();
            props.load(inputStream);
        } catch (Throwable t) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable ignored) {
                }
            }
        }
        return props;
    }
