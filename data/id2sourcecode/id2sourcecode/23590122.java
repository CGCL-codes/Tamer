    private static void init() {
        ServiceLocator.client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            final Enumeration<URL> URLs = Thread.currentThread().getContextClassLoader().getResources("config/service-locator.xml");
            for (; URLs.hasMoreElements(); ) {
                InputStream in = null;
                try {
                    final URL url = URLs.nextElement();
                    in = url.openStream();
                    if (ServiceLocator.config == null) {
                        ServiceLocator.config = BaseXMLSerializer.fromXML(in, ServiceLocatorConfig.class);
                    } else {
                        final ServiceLocatorConfig nconfig = BaseXMLSerializer.fromXML(in, ServiceLocatorConfig.class);
                        for (final Map.Entry<String, URL> entry : nconfig.getMapServiceUrl().entrySet()) {
                            ServiceLocator.config.getMapServiceUrl().put(entry.getKey(), entry.getValue());
                        }
                    }
                } finally {
                    try {
                        in.close();
                    } catch (final Exception ignore) {
                    }
                }
            }
        } catch (final Exception ignore) {
        }
        try {
            final Context initCtx = new InitialContext();
            final Context envCtx = (Context) initCtx.lookup("java:comp/env");
            final JNDIPropertiesConfig jndiProps = (JNDIPropertiesConfig) envCtx.lookup("alcrest/serviceslocator");
            final ServiceLocatorConfig slb = new ServiceLocatorConfig();
            slb.setServicesProperties(jndiProps.getContentAsProperties());
            if (ServiceLocator.config == null) {
                ServiceLocator.config = slb;
            } else {
                for (final Map.Entry<String, URL> entry : slb.getMapServiceUrl().entrySet()) {
                    ServiceLocator.config.getMapServiceUrl().put(entry.getKey(), entry.getValue());
                }
            }
        } catch (final Exception ignore) {
        }
    }
