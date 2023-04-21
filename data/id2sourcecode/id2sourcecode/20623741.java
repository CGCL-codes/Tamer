    private static Properties loadJAXBProperties(ClassLoader classLoader, String propFileName) throws JAXBException {
        Properties props = null;
        try {
            URL url;
            if (classLoader == null) url = ClassLoader.getSystemResource(propFileName); else url = classLoader.getResource(propFileName);
            if (url != null) {
                logger.fine("loading props from " + url);
                props = new Properties();
                InputStream is = url.openStream();
                props.load(is);
                is.close();
            }
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Unable to load " + propFileName, ioe);
            throw new JAXBException(ioe.toString(), ioe);
        }
        return props;
    }
