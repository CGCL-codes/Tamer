    @SuppressWarnings("unchecked")
    public static <T> Set<T> set(final Class<T> clazz) throws ServiceException {
        final ClassLoader loader = Util.getClassLoader(null);
        final Enumeration<URL> resources = getServiceResources(clazz, loader);
        final Set<T> serviceSet = new HashSet<T>();
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
                    try {
                        serviceClass = (Class<T>) loader.loadClass(line);
                    } catch (final ClassNotFoundException e) {
                        final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0014");
                        logRecord.setParameters(new Object[] { line });
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
                        logRecord.setParameters(new Object[] { line });
                        logRecord.setThrown(e);
                        logRecord.setResourceBundleName(logger.getResourceBundleName());
                        logRecord.setResourceBundle(logger.getResourceBundle());
                        logger.log(logRecord);
                        continue;
                    }
                    serviceSet.add(instance);
                }
            } catch (final IOException e) {
                final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0012");
                logRecord.setParameters(new Object[] { url });
                logRecord.setThrown(e);
                logRecord.setResourceBundleName(logger.getResourceBundleName());
                logRecord.setResourceBundle(logger.getResourceBundle());
                logger.log(logRecord);
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
        return Collections.unmodifiableSet(serviceSet);
    }
