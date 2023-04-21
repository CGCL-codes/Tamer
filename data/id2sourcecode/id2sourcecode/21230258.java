    private void configure() {
        Bundle thisBundle = Platform.getBundle(TCardCore.PLUGIN_ID);
        try {
            URL url = thisBundle.getEntry("/" + LOG_PROPERTIES_FILE);
            InputStream propertiesInputStream = url.openStream();
            if (propertiesInputStream != null) {
                Properties props = new Properties();
                props.load(propertiesInputStream);
                propertiesInputStream.close();
                this.logManager = new PluginLogManager(thisBundle, props);
            }
        } catch (Exception e) {
            String message = "Error while initializing log properties." + e.getMessage();
            IStatus status = new Status(IStatus.ERROR, thisBundle.getSymbolicName(), IStatus.ERROR, message, e);
            Platform.getLog(thisBundle).log(status);
            throw new RuntimeException("Error while initializing log properties.", e);
        }
    }
