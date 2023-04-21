    public TemplateResolver() throws IOException {
        logger.info("Init TemplateResolver");
        path = new LinkedList<String>();
        path.add("");
        for (Enumeration<URL> urlEnum = Thread.currentThread().getContextClassLoader().getResources("META-INF/template-resolver.properties"); urlEnum.hasMoreElements(); ) {
            Properties properties = new Properties();
            properties.load(urlEnum.nextElement().openStream());
            if (properties.containsKey("path")) {
                path.add(properties.getProperty("path"));
            }
        }
        logger.info("TemplateResolver initialised");
    }
