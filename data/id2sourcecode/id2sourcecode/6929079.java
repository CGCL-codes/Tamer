    public void start(BundleContext bundleContext) throws Exception {
        configDir = System.getProperty(ARGUMENT_CONFIG_DIR, COOS_CONFIG_DIR);
        logger.info("Coos OSGI starting");
        this.bc = bundleContext;
        InputStream is = null;
        try {
            is = new FileInputStream(configDir + COOS_CONFIG_FILE);
            is = substitute(is);
        } catch (Exception e) {
        }
        if (is != null) {
            logger.info("Using provided coos config");
        } else {
            URL url = bc.getBundle().getResource(COOS_CONFIG_PATH + COOS_CONFIG_FILE);
            if (url == null) {
                logger.warn("This coos bundle has no Coos configuration!");
                return;
            }
            is = url.openStream();
            is = substitute(is);
            logger.info("Using included coos config");
        }
        coos = COOSFactory.createCOOS(is);
        coos.start();
        ChannelServer channelServer = coos.getChannelServer("default");
        bc.registerService(ChannelServer.class.getName(), channelServer, new Hashtable());
        logger.info("ChannelServer registered");
        Enumeration<URL> urls = bc.getBundle().findEntries(COOS_CONFIG_PATH, "plugin*.xml", false);
        if (urls != null && urls.hasMoreElements()) {
            logger.info("Starting included plugins");
        } else {
            return;
        }
        while (urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            is = null;
            String fileName = null;
            try {
                fileName = url.getFile();
                fileName = fileName.substring(fileName.lastIndexOf("/"));
                logger.info("Looking for plugin configurations in: " + configDir + fileName);
                is = new FileInputStream(configDir + fileName);
                is = substitute(is);
            } catch (Exception e) {
            }
            if (is == null) {
                is = url.openStream();
                is = substitute(is);
                logger.info("Starting plugins defined in: " + url.getFile());
            } else {
                logger.info("Starting plugins defined in: " + configDir + fileName);
            }
            Plugin[] plg = PluginFactory.createPlugins(is, this);
            for (int i = 0; i < plg.length; i++) {
                Plugin plugin = plg[i];
                for (Iterator iterator = plugin.getChannels().iterator(); iterator.hasNext(); ) {
                    Channel channel = (Channel) iterator.next();
                    if (channel instanceof PluginChannel && ((DefaultChannel) channel).getTransport() instanceof JvmTransport) {
                        ((JvmTransport) ((DefaultChannel) channel).getTransport()).setChannelServer(channelServer);
                    }
                }
                plugin.connect();
            }
            for (int i = 0; i < plg.length; i++) {
                plugins.add(plg[i]);
            }
        }
    }
