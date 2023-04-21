    protected Configuration createDefaultHierarchicalConfiguration(final String staticConfig, final String userConfig, final boolean addSysProps, final Class source) {
        final HierarchicalConfiguration globalConfig = new HierarchicalConfiguration();
        if (staticConfig != null) {
            final PropertyFileConfiguration rootProperty = new PropertyFileConfiguration();
            rootProperty.load(staticConfig, getClass());
            globalConfig.insertConfiguration(rootProperty);
            globalConfig.insertConfiguration(getPackageManager().getPackageConfiguration());
        }
        if (userConfig != null) {
            String userConfigStripped;
            if (userConfig.startsWith("/")) {
                userConfigStripped = userConfig.substring(1);
            } else {
                userConfigStripped = userConfig;
            }
            try {
                final Enumeration userConfigs = ObjectUtilities.getClassLoader(getClass()).getResources(userConfigStripped);
                final ArrayList configs = new ArrayList();
                while (userConfigs.hasMoreElements()) {
                    final URL url = (URL) userConfigs.nextElement();
                    try {
                        final PropertyFileConfiguration baseProperty = new PropertyFileConfiguration();
                        final InputStream in = url.openStream();
                        baseProperty.load(in);
                        in.close();
                        configs.add(baseProperty);
                    } catch (IOException ioe) {
                        Log.warn("Failed to load the user configuration at " + url, ioe);
                    }
                }
                for (int i = configs.size() - 1; i >= 0; i--) {
                    final PropertyFileConfiguration baseProperty = (PropertyFileConfiguration) configs.get(i);
                    globalConfig.insertConfiguration(baseProperty);
                }
            } catch (IOException e) {
                Log.warn("Failed to lookup the user configurations.", e);
            }
        }
        if (addSysProps) {
            final SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
            globalConfig.insertConfiguration(systemConfig);
        }
        return globalConfig;
    }
