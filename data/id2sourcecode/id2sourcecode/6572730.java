    protected Properties getProperties() {
        if (abbrvProperties == null) {
            try {
                abbrvProperties = new Properties();
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Enumeration<URL> files = classLoader.getResources("abbreviation.properties");
                while (files.hasMoreElements()) {
                    URL url = (URL) files.nextElement();
                    log.debug("ABBRV URL: {0}", url);
                    InputStream resourceAsStream = url.openStream();
                    try {
                        Properties temp = new Properties();
                        temp.load(resourceAsStream);
                        Set<?> names = sort(temp);
                        for (Object name : names) {
                            if (abbrvProperties.containsKey(name)) {
                                Object value1 = abbrvProperties.get(name);
                                Object value2 = temp.get(name);
                                if (!value1.equals(value2)) {
                                    throw new AssertionFailure("Duplicate Abbreviation Found: " + name + "=" + value2 + ", in: " + url);
                                } else {
                                    log.debug("Duplicate Abbreviation Found: {0}={1}", name, value2);
                                }
                            } else {
                                abbrvProperties.put(name, temp.get(name));
                            }
                        }
                    } finally {
                        resourceAsStream.close();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to load abbreviation properties, ignore it.", e);
            }
        }
        return abbrvProperties;
    }
