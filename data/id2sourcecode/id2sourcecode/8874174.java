    public static void lookup() {
        File pluginDirectory = new File(PMS.getConfiguration().getPluginDirectory());
        LOGGER.info("Searching for plugins in " + pluginDirectory.getAbsolutePath());
        if (!pluginDirectory.exists()) {
            LOGGER.warn("Plugin directory doesn't exist: " + pluginDirectory);
            return;
        }
        if (!pluginDirectory.isDirectory()) {
            LOGGER.warn("Plugin directory is not a directory: " + pluginDirectory);
            return;
        }
        File[] jarFiles = pluginDirectory.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.isFile() && file.getName().toLowerCase().endsWith(".jar");
            }
        });
        int nJars = jarFiles.length;
        if (nJars == 0) {
            LOGGER.info("No plugins found");
            return;
        }
        List<URL> jarURLList = new ArrayList<URL>();
        for (int i = 0; i < nJars; ++i) {
            try {
                jarURLList.add(jarFiles[i].toURI().toURL());
            } catch (MalformedURLException e) {
                LOGGER.error("Can't convert file path " + jarFiles[i] + " to URL", e);
            }
        }
        URL[] jarURLs = new URL[jarURLList.size()];
        jarURLList.toArray(jarURLs);
        URLClassLoader classLoader = new URLClassLoader(jarURLs);
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources("plugin");
        } catch (IOException e) {
            LOGGER.error("Can't load plugin resources", e);
            return;
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                InputStreamReader in = new InputStreamReader(url.openStream());
                char[] name = new char[512];
                in.read(name);
                in.close();
                String pluginMainClassName = new String(name).trim();
                LOGGER.info("Found plugin: " + pluginMainClassName);
                Class<?> clazz = classLoader.loadClass(pluginMainClassName);
                registerListenerClass(clazz);
            } catch (Exception e) {
                LOGGER.error("Error loading plugin", e);
            } catch (NoClassDefFoundError e) {
                LOGGER.error("Error loading plugin", e);
            }
        }
        instantiateEarlyListeners();
    }
