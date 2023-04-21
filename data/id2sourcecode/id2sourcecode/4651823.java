    @Inject
    public BeanXStreamConverter(XStreamConfiguration configuration) {
        rp = new PureJavaReflectionProvider();
        Mapper dmapper = new DefaultMapper(this.getClass().getClassLoader());
        writerStack = new ThreadSafeWriterStack();
        driver = new StackDriver(new XppDriver(), writerStack, configuration.getNameSpaces());
        for (XStreamConfiguration.ConverterSet c : MAPPER_SCOPES) {
            converterMap.put(c, configuration.getConverterConfig(c, rp, dmapper, driver, writerStack));
        }
    }
