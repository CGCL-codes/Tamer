    public static Plugin[] startPlugins(String configDir, COContainer container) throws Exception {
        InputStream is = null;
        pluginConfigFiles = new ArrayList<URL>();
        String[] files = getResources();
        for (int i = 0; i < files.length; i++) {
            if (files[i].matches(CONFIG_FILE_PATTERN)) {
                addPluginConfigFiles(COOSBootstrapHelper.class.getResource(COOS_CONFIG_PATH + "/" + files[i]));
            }
        }
        File dir = new File(configDir);
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].getName().matches(CONFIG_FILE_PATTERN)) {
                    addPluginConfigFiles(fileList[i].toURI().toURL());
                }
            }
        }
        if (!pluginConfigFiles.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            for (URL conf : pluginConfigFiles) {
                buf.append(conf);
                buf.append("\n");
            }
            logger.info("Starting COOS plugins defined in: \n" + buf.toString());
        }
        Plugin[] plugins = null;
        ArrayList<Plugin> pluginArray = new ArrayList<Plugin>();
        for (Iterator<URL> iterator = pluginConfigFiles.iterator(); iterator.hasNext(); ) {
            URL url = iterator.next();
            is = url.openStream();
            is = substitute(is);
            plugins = PluginFactory.createPlugins(is, container);
            for (int i = 0; i < plugins.length; i++) {
                pluginArray.add(plugins[i]);
            }
        }
        plugins = pluginArray.toArray(new Plugin[0]);
        Plugin[] sortedPlugins = new Plugin[plugins.length];
        int lowest = Integer.MAX_VALUE;
        int lowestIdx = 0;
        for (int i = 0; i < sortedPlugins.length; i++) {
            for (int j = 0; j < plugins.length; j++) {
                if ((plugins[j] != null) && (plugins[j].getStartLevel() < lowest)) {
                    lowest = plugins[j].getStartLevel();
                    lowestIdx = j;
                }
            }
            sortedPlugins[i] = plugins[lowestIdx];
            plugins[lowestIdx] = null;
            lowest = Integer.MAX_VALUE;
        }
        plugins = sortedPlugins;
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            try {
                plugin.connect();
            } catch (ConnectingException e) {
                logger.error("ConnectingException ignored", e);
            }
        }
        return plugins;
    }
