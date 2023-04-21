    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        try {
            URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
            if (url != null) {
                InputStream propertiesInputStream = url.openStream();
                LogManager.getLogManager().readConfiguration(propertiesInputStream);
                propertiesInputStream.close();
            }
        } catch (IOException lEx) {
            log(IStatus.WARNING, "Couldn't load a resource file " + LOG_PROPERTIES_FILE + ".", lEx);
        }
    }
