    @SuppressWarnings({ "ConstantConditions" })
    protected final void configure() {
        Enumeration<URL> resources;
        try {
            resources = getClassLoader().getResources(getResourceName());
        } catch (IOException e) {
            binder().addError(e.getMessage(), e);
            return;
        }
        int resourceCount = 0;
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            log.debug(url + " ...");
            try {
                InputStream stream = url.openStream();
                configureFromResource(stream, url);
                resourceCount++;
            } catch (Exception e) {
                final String msg = "Error configuring from " + url + ": " + e.getMessage();
                log.error(msg, e);
                binder().addError(msg, e);
            }
        }
        log.info("Added components from " + resourceCount + " resources.");
    }
