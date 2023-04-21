    public void start(BundleContext context) throws Exception {
        super.start(context);
        String pluginlogfile = "";
        plugin = this;
        try {
            URL url = this.getClass().getResource("/net/hakulaite/maverick/log4j.properties");
            Properties properties = new Properties();
            properties.load(url.openStream());
            pluginlogfile = Platform.getPluginStateLocation(this) + "/" + properties.getProperty("log4j.appender.R.File");
            if (Platform.inDebugMode()) {
                System.out.println("Opening plugin log file: " + pluginlogfile);
            }
            properties.setProperty("log4j.appender.R.File", pluginlogfile);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            System.err.println("Could not initialize logger, beacuse " + e);
        }
        log.info("Initializing maverick plugin");
        IContainer root = getWorkspace().getRoot();
        log.debug("Searching for maverick.xml");
        find(root, "maverick.xml");
        log.debug("Search completed");
        ImageRegistry ireg = getImageRegistry();
        try {
            resourceBundle = ResourceBundle.getBundle("net.hakulaite.maverick.resources");
        } catch (MissingResourceException x) {
            log.error("Cannot load resources 'net.hakulaite.maverick.resources'");
            resourceBundle = null;
        }
        String imgprefix = getResourceString("plugin.images.prefix");
        String imgpostfix = getResourceString("plugin.images.url.postfix");
        String preloadimages = getResourceString("plugin.preloadimages");
        StringTokenizer st = new StringTokenizer(preloadimages, ",");
        while (st.hasMoreElements()) {
            String imagename = (String) st.nextElement();
            String imageurl = getResourceString(imgprefix + "." + imagename + "." + imgpostfix);
            if (imagename != null && imageurl != null) addImageToRegistry(imagename, imageurl, ireg);
        }
        checkClasspathVariable();
        getWorkspace().addResourceChangeListener(new MaverickResourceChangeReporter(), IResourceChangeEvent.POST_CHANGE);
        log.info("Initialization complete.");
    }
