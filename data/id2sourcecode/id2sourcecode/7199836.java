    public static Logger getLogger(String name) {
        if (!isLoadedExternally) {
            if (LogManager.exists(name) != null) {
                logger = LogManager.exists(name);
            } else {
                if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
                    BasicConfigurator.configure();
                } else {
                    URL url = ClassLoader.getSystemResource("config/jsf.log4j.properties");
                    Properties log4jProps = new Properties();
                    try {
                        log4jProps.load(url.openStream());
                        PropertyConfigurator.configure(log4jProps);
                    } catch (Exception e) {
                        BasicConfigurator.configure();
                    }
                }
                Log4jManager.alreadyLoaded = true;
                logger = Logger.getLogger(name);
            }
        } else {
            logger = Logger.getLogger(name);
        }
        return logger;
    }
