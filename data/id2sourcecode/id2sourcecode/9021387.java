    private synchronized void configureLoggingEnvironment(AphisOriginator comp) throws AphisFactoryException {
        if (comp == null) {
            throw new AphisFactoryException("Could not configure logging environment, component was null");
        }
        if (confMgr == null) {
            confMgr = AphisConfigurationManager.getInstance(comp);
        }
        try {
            String newConfigLoc = confMgr.getConfigurationItem(LOG4J_CONF_PARAM_NAME, null);
            if (newConfigLoc != null) {
                try {
                    URL url = new URL(newConfigLoc);
                    if (url.toExternalForm().endsWith("xml")) {
                        DOMConfigurator.configure(url);
                    } else {
                        Properties p = new Properties();
                        try {
                            p.load(url.openConnection().getInputStream());
                            PropertyConfigurator.configure(p);
                        } catch (Exception ex) {
                            throw new AphisFactoryException("Could not configure log4j using remote properties file", ex);
                        }
                    }
                } catch (Exception e) {
                    try {
                        PropertyConfigurator.configure(newConfigLoc);
                    } catch (Exception ex) {
                        throw new AphisFactoryException("Could not configure log4j using configuration files", ex);
                    }
                }
            } else {
                throw new AphisFactoryException("log4j configuration file not provided, aborting");
            }
        } catch (Exception e) {
            throw new AphisFactoryException("Could not configure logging environment", e);
        }
    }
