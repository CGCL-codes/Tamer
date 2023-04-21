    protected static SelectorThread startServer() throws IOException {
        final Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages", "net.sourceforge.ondex.restful.resources" + ";" + "net.sourceforge.ondex.restful.resources.writers" + ";" + "net.sourceforge.ondex.restful.resources.readers" + ";" + "net.sourceforge.ondex.restful.resources.injectable");
        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(BASE_URI, initParams);
        return threadSelector;
    }
