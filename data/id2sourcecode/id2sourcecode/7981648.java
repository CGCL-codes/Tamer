        @Override
        public void start(BundleContext aContext) throws Exception {
            super.start(aContext);
            try {
                URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
                if (url != null) {
                    InputStream propertiesInputStream = url.openStream();
                    LogManager.getLogManager().readConfiguration(propertiesInputStream);
                    propertiesInputStream.close();
                }
            } catch (IOException lEx) {
                EPFECoreActivator.log(IStatus.WARNING, "Couldn't load a resource file " + LOG_PROPERTIES_FILE + ".", lEx);
            }
            fResListenerTracker = new EPFEResourceChangeDelegateServiceTracker(aContext);
            registerServices(aContext);
        }
