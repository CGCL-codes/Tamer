    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
        Properties properties = new Properties();
        for (IConfigurationElement e : Platform.getExtensionRegistry().getConfigurationElementsFor(CFG_EXT_POINT)) {
            Properties local = new Properties();
            String url = e.getAttribute("url");
            if (url != null) {
                local.load(new URL(url).openStream());
            }
            String res = e.getAttribute("resource");
            if (res != null) {
                Bundle b = Platform.getBundle(e.getDeclaringExtension().getNamespaceIdentifier());
                local.load(b.getEntry(res).openStream());
            }
            for (Object k : local.keySet()) {
                String key = (String) k;
                String val = local.getProperty(key);
                if (val == null) continue;
                val = mgr.performStringSubstitution(val);
                properties.setProperty(key, val);
            }
        }
        PropertyConfigurator.configure(properties);
    }
