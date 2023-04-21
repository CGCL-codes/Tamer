    public void init(ServletConfig config) {
        LOG.info("[org.rendersnake.annotation.URIMappingResolver.init] Reading mapping file");
        try {
            Properties configuration = new Properties();
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(CONFIG_PROPERTIES);
            while (urls.hasMoreElements()) {
                InputStream is = urls.nextElement().openStream();
                configuration.load(is);
                is.close();
            }
            for (Object each : configuration.keySet()) {
                String key = (String) each;
                this.tryAddMapping(configuration, key);
            }
        } catch (Exception ex) {
            throw new RuntimeException("[URIMappingResolver.init(config)] failed", ex);
        }
        LOG.info("[URIMappingResolver.init] mappings loaded:" + uriToClassMap.size());
    }
