    public static synchronized <T> T build(final Class<T> type) {
        if (!configurations.containsKey(type)) {
            Configuration annotation = type.getAnnotation(Configuration.class);
            if (annotation == null) {
                throw new IllegalConfigurationException("Expected " + Configuration.class.getCanonicalName() + "annotation for interface " + type.getCanonicalName());
            }
            Data.Configuration configuration = new Data.Configuration(annotation.value());
            ConfigUtils.build(type, configuration);
            try {
                final ConfigurationReader reader = readerFactory.newReader(type);
                ConfigurationData data = reader.read(annotation.value());
                configuration.update(data);
                final ConfigurationWriter writer = writerFactory.newWriter(type);
                writer.write(configuration);
            } catch (IOException e) {
                exceptionListener.exceptionThrown(e);
            }
            configuration.addConfigurationListener(new ReadWriteConfigurationListener(type, readerFactory, writerFactory, exceptionListener));
            if (configListener != null) {
                configuration.addConfigurationListener(configListener);
            }
            SectionsHandler handler = new SectionsHandler(configuration);
            Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type, Observable.class }, handler);
            configurations.put(type, proxy);
        }
        return type.cast(configurations.get(type));
    }
