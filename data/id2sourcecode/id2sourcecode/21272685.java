    public static InputStream getConfigFile(final String fileName) {
        final File log4jFile = new File(SystemProperty.getProperty(SystemProperty.WEBMOTIX_APP_ROOTDIR), fileName);
        InputStream stream;
        if (log4jFile.exists()) {
            URL url;
            try {
                url = new URL("file:" + log4jFile.getAbsolutePath());
            } catch (final MalformedURLException e) {
                log.error("Unable to read config file from [" + fileName + "], got a MalformedURLException: ", e);
                return null;
            }
            try {
                stream = url.openStream();
            } catch (final IOException e) {
                log.error("Unable to read config file from [" + fileName + "], got a IOException ", e);
                return null;
            }
        } else {
            try {
                stream = new FileInputStream(fileName);
            } catch (final FileNotFoundException e) {
                log.error("Unable to read config file from [" + fileName + "], got a FileNotFoundException ", e);
                return null;
            }
        }
        return stream;
    }
