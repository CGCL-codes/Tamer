    private Configuration(String resource) {
        Enumeration<URL> configurations;
        try {
            configurations = getClass().getClassLoader().getResources(resource);
        } catch (IOException e1) {
            LOG.fatal("Could not read config.");
            return;
        }
        while (configurations.hasMoreElements()) {
            Properties properties = new Properties();
            InputStream is = null;
            URL url = null;
            try {
                url = configurations.nextElement();
                is = url.openStream();
                properties.load(is);
                this.configurations.put(url, properties);
            } catch (Exception e) {
                LOG.error("Could not load configuration " + url, e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOG.warn("Error closing resource " + url, e);
                    }
                }
            }
        }
    }
