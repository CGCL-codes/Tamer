    @Override
    protected void configure() {
        try {
            URL url = BootModule.class.getResource(INT_PROPERTIES);
            InputStream in = url.openStream();
            props.load(in);
            logger.config("load init properties " + url);
            Names.bindProperties(binder(), props);
        } catch (Exception e) {
            logger.finest("classpath:/init.properties not exist, use out-of-box values");
        }
        loadFromClasspath();
    }
