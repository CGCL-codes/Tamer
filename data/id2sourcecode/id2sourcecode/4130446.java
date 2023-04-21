    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> map(final Class<T> clazz) throws ServiceException {
        final ClassLoader loader = Util.getClassLoader(null);
        final Enumeration<URL> resources = getServiceResources(clazz, loader);
        final Map<String, T> serviceMap = new LinkedHashMap<String, T>();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            BufferedReader reader = null;
            try {
                String line;
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                while (null != (line = reader.readLine())) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    Class<T> serviceClass;
                    final String[] record = line.split(":");
                    try {
                        serviceClass = (Class<T>) loader.loadClass(record[1]);
                    } catch (final ClassNotFoundException e) {
                        final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0011");
                        logRecord.setParameters(new Object[] { record[1], record[0] });
                        logRecord.setThrown(e);
                        logRecord.setResourceBundleName(logger.getResourceBundleName());
                        logRecord.setResourceBundle(logger.getResourceBundle());
                        logger.log(logRecord);
                        continue;
                    }
                    T instance;
                    try {
                        instance = serviceClass.newInstance();
                    } catch (final Exception e) {
                        final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0015");
                        logRecord.setParameters(new Object[] { record[1] });
                        logRecord.setThrown(e);
                        logRecord.setResourceBundleName(logger.getResourceBundleName());
                        logRecord.setResourceBundle(logger.getResourceBundle());
                        logger.log(logRecord);
                        continue;
                    }
                    serviceMap.put(("".equals(record[0]) ? null : record[0]), instance);
                }
            } catch (final IOException e) {
                final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0012");
                logRecord.setParameters(new Object[] { url });
                logRecord.setThrown(e);
                logRecord.setResourceBundleName(logger.getResourceBundleName());
                logRecord.setResourceBundle(logger.getResourceBundle());
                logger.log(logRecord);
                continue;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        ;
                    }
                }
            }
        }
        return Collections.unmodifiableMap(serviceMap);
    }
