    private static void loadProperties() throws FileNotFoundException, IOException {
        URL url = null;
        try {
            url = new URL(configFile);
        } catch (MalformedURLException e) {
            url = new File(configFile).toURI().toURL();
        }
        logger.config(Localizer.getLocalizedMessage(Service.class, "loadProperties.logger.config", url));
        InputStream in = url.openStream();
        loadProperties(in);
        in.close();
    }
