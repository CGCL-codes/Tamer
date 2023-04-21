    private static void loadServiceClasses() throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("loadServiceClasses");
        }
        ClassLoader cl = Services.class.getClassLoader();
        Enumeration<URL> urls = cl.getResources(SERVICES_RESOURCE);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found Services.properties: " + url);
            }
            InputStream in = url.openStream();
            try {
                Properties props = new Properties();
                props.load(in);
                String s = props.getProperty(PROPERTY_SERVICES);
                if (null == s) {
                    LOG.warn("Property '" + PROPERTY_SERVICES + "' not specified in resource '" + url + "'");
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("services=" + s);
                    }
                    String[] classNames = s.split(CLASS_NAME_SEPARATOR);
                    for (String className : classNames) {
                        try {
                            loadServiceClass(className);
                        } catch (ClassNotFoundException ex) {
                            LOG.error("An exception occurred while loading class " + className + " from resource " + url, ex);
                        }
                    }
                }
            } finally {
                IOUtil.closeSilently(in);
            }
        }
    }
