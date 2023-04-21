    private Version() {
        propVersion = null;
        InputStream stream;
        try {
            URL url = this.getClass().getResource("/version.xml");
            log.info(url);
            if (url != null) {
                stream = url.openStream();
                propVersion = new Properties();
                propVersion.loadFromXML(stream);
            }
        } catch (FileNotFoundException e) {
            log.warn("version.xml file not found. Don't know which version I am.", e);
        } catch (InvalidPropertiesFormatException e) {
            log.warn("version.xml file not found. Don't know which version I am.", e);
        } catch (IOException e) {
            log.warn("version.xml file not found. Don't know which version I am.", e);
        }
        appVersion = "Development version";
        if (propVersion != null) {
            String version = propVersion.getProperty("version");
            if (version != null && (!version.trim().equals("") || !version.trim().equals("@@@VERSION@@@"))) {
                appVersion = version;
            }
        } else {
            log.warn("No new properties. Is the file version.xml in the jar at all?");
        }
    }
