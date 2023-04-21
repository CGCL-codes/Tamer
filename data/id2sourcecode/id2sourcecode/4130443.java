    @SuppressWarnings("unchecked")
    public static <T> T chain(final Class<T> clazz, final T defaultInstance) throws ServiceException {
        final ClassLoader loader = Util.getClassLoader(null);
        final Enumeration<URL> resources = getServiceResources(clazz, loader);
        T current = defaultInstance;
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
                    Constructor<T> constructor;
                    try {
                        if (current == null) {
                            constructor = serviceClass.getConstructor();
                        } else {
                            constructor = serviceClass.getConstructor(clazz);
                        }
                    } catch (Exception e) {
                        final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0027");
                        logRecord.setParameters(new Object[] { line });
                        logRecord.setThrown(e);
                        logRecord.setResourceBundleName(logger.getResourceBundleName());
                        logRecord.setResourceBundle(logger.getResourceBundle());
                        logger.log(logRecord);
                        continue;
                    }
                    T instance;
                    try {
                        if (current == null) {
                            instance = constructor.newInstance();
                        } else {
                            instance = constructor.newInstance(current);
                        }
                    } catch (Exception e) {
                        final LogRecord logRecord = new LogRecord(Level.SEVERE, "JRJSF_0015");
                        logRecord.setParameters(new Object[] { line });
                        logRecord.setThrown(e);
                        logRecord.setResourceBundleName(logger.getResourceBundleName());
                        logRecord.setResourceBundle(logger.getResourceBundle());
                        logger.log(logRecord);
                        continue;
                    }
                    current = instance;
                }
            } catch (IOException e) {
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
                    } catch (IOException e) {
                        ;
                    }
                    reader = null;
                }
            }
        }
        return current;
    }
