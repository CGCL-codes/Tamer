    private Properties loadPropertiesFromClasspath(Properties p_defaultProperties) {
        Properties props = new Properties();
        if (p_defaultProperties != null) props.putAll(p_defaultProperties);
        try {
            Enumeration<URL> en = LoadingUtils.getResources(m_filePath);
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                logger.info("Found file " + url.toExternalForm());
                InputStream inputStream = null;
                try {
                    inputStream = url.openStream();
                    logger.info("Opening file " + url.toExternalForm());
                    if (inputStream != null) {
                        props = loadProperties(inputStream, props);
                        logger.info("Loaded " + url.getFile() + " from CLASSPATH " + url.getPath());
                    }
                } catch (IOException e) {
                    logger.warn("Unable to load  \"" + url.toExternalForm() + "\" from CLASSPATH.", e);
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (IOException e) {
                        logger.debug(e);
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Unable to retrieve resource " + m_filePath + " due to a " + e.getClass().getName() + " with message '" + e.getMessage() + "'");
        }
        return props;
    }
