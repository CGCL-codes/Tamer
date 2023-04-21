    HibernateUtil() {
        _classes = new ArrayList<Class<?>>();
        try {
            _config = new AnnotationConfiguration();
            final SAXReader r = new SAXReader();
            final InputStream cfgXml = this.getClass().getResourceAsStream("hibernate.cfg.xml");
            final Document x = new DOMWriter().write(r.read(cfgXml));
            _config.configure(x);
            configConnection();
            final IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint("net.sf.myway.hibernate.provider");
            final IExtension[] extensions = point.getExtensions();
            _config.addAnnotatedClass(UuidEntity.class);
            _config.addAnnotatedClass(NamedUuidEntity.class);
            for (final IExtension ext : extensions) {
                final IConfigurationElement[] elements = ext.getConfigurationElements();
                for (final IConfigurationElement ce : elements) {
                    final EntityClassProvider iFace = (EntityClassProvider) ce.createExecutableExtension("class");
                    for (final Class<?> c : iFace.getEntityClasses()) {
                        _classes.add(c);
                        _config.addAnnotatedClass(c);
                    }
                }
            }
        } catch (final Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
