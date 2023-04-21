    public static Properties loadPropertiesFile(String fileName) throws Exception {
        Properties properties = null;
        URL url = checkJarFile(fileName);
        File file = checkFsFile(fileName);
        try {
            if (url != null) {
                properties = new Properties();
                properties.load(url.openStream());
            } else if (file != null) {
                properties = new Properties();
                properties.load(new FileInputStream(file));
            } else {
                logger.info("properties file: " + fileName + " NOT found");
                throw new Exception(fileName + " NOT Found");
            }
        } catch (IOException ioe) {
            throw new Exception("IOException loading " + fileName + "\t " + ioe);
        }
        logger.info(fileName + " file LOADED");
        return properties;
    }
