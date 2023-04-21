    private void retrieveAndCallInitializers(Application application, WiQuerySettings wiQuerySettings) {
        try {
            final Iterator<URL> resources = application.getApplicationSettings().getClassResolver().getResources("wiquery.properties");
            while (resources.hasNext()) {
                InputStream in = null;
                try {
                    final URL url = resources.next();
                    final Properties properties = new Properties();
                    in = url.openStream();
                    properties.load(in);
                    load(application, wiQuerySettings, properties);
                } finally {
                    IOUtils.close(in);
                }
            }
        } catch (IOException e) {
            throw new WicketRuntimeException("Unable to load initializers file", e);
        }
        callInitializers(application, wiQuerySettings);
    }
