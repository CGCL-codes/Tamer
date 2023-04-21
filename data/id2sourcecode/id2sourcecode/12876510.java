    public static Properties loadAllProperties(final Enumeration<URL> urls) throws IOException {
        final Properties allProperties = new Properties();
        while (urls.hasMoreElements()) {
            final URL url = urls.nextElement();
            final Properties properties = new Properties();
            final InputStream inputStream = url.openStream();
            try {
                properties.load(inputStream);
            } finally {
                inputStream.close();
            }
            for (final Enumeration<?> enNames = properties.propertyNames(); enNames.hasMoreElements(); ) {
                final String name = (String) enNames.nextElement();
                final String value;
                if (!allProperties.containsKey(name)) {
                    value = properties.getProperty(name);
                } else {
                    value = allProperties.getProperty(name) + ", " + properties.getProperty(name);
                }
                allProperties.setProperty(name, value);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loaded the following properties:");
            for (final Enumeration<?> enNames = allProperties.propertyNames(); enNames.hasMoreElements(); ) {
                final String name = (String) enNames.nextElement();
                final String value = allProperties.getProperty(name);
                LOGGER.debug(String.format("%s=%s", name, value));
            }
        }
        return allProperties;
    }
