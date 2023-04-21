    private void configure() {
        try {
            URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
            InputStream propertiesInputStream = url.openStream();
            if (propertiesInputStream != null) {
                Properties props = new Properties();
                props.load(propertiesInputStream);
                propertiesInputStream.close();
                this.logManager = new PluginLogManager(this, props);
                this.logManager.hookPlugin(Activator.getDefault().getBundle().getSymbolicName(), Activator.getDefault().getLog());
            }
        } catch (Exception e) {
            String message = "Error while initializing log properties." + e.getMessage();
            IStatus status = new Status(IStatus.ERROR, getDefault().getBundle().getSymbolicName(), IStatus.ERROR, message, e);
            getLog().log(status);
            throw new RuntimeException("Error while initializing log properties.", e);
        }
    }
