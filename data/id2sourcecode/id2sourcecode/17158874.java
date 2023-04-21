    public void setConfigurationFilePath(String configurationFilePath) {
        try {
            URL url = ResourceLoader.getInstance().getRcResource(configurationFilePath);
            Properties properties = new Properties();
            properties.load(url.openStream());
            String factoryClassName = properties.getProperty("layout.factory");
            if (factoryClassName == null) {
                logger.error("could not find parameter 'layout.factory' from resource '" + configurationFilePath + "'");
            } else {
                try {
                    Class factoryClass = ResourceLoader.getInstance().getClass(factoryClassName);
                    Object o = factoryClass.newInstance();
                    if (o instanceof ViewLayoutFactory) {
                        ViewLayout layout = ((ViewLayoutFactory) o).createViewLayout(properties);
                        this.setViewLayout(layout);
                        logger.info("setting layout " + layout + " for docking panel " + this);
                    } else {
                        logger.error("could not configure a docking panel with a non layout factory " + o);
                    }
                } catch (ResourceException e) {
                    logger.error("could not load class '" + factoryClassName, e);
                } catch (Exception e) {
                    logger.error("could not create an instance of class '" + factoryClassName, e);
                }
            }
        } catch (ResourceException e) {
            logger.error("could not load docking panel configuration file '" + configurationFilePath, e);
        } catch (IOException e) {
            logger.error("could not open stream on resource '" + configurationFilePath, e);
        }
    }
