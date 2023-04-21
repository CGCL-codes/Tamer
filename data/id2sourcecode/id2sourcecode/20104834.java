    public static Properties loadProperties(String propFileName) {
        Properties props = new Properties();
        String propPath = "properties/" + propFileName;
        try {
            Enumeration e = ClassLoader.getSystemResources(propPath);
            if (!e.hasMoreElements()) {
                logger.warning("Can't find property file " + propPath);
                logger.warning("Please create one and place in your classpath");
            } else {
                while (e.hasMoreElements()) {
                    URL url = (URL) e.nextElement();
                    System.out.println("Loading properties from " + url);
                    props.load(url.openStream());
                }
            }
        } catch (IOException e1) {
            logger.warning("Can't find property file " + propPath);
            logger.warning("Please create one and place in your classpath");
        }
        if (envMap != null) replaceEnvVariables(props);
        if (logger.isLoggable(Level.FINE)) props.list(System.out);
        return props;
    }
