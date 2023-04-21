    static Hashtable<Object, Object> readMultipleResourceFiles(final String name, final Hashtable<Object, Object> existingProps, ClassLoader cl) throws NamingException {
        if (null == cl) {
            cl = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> e = null;
        try {
            e = cl.getResources(name);
        } catch (IOException ex) {
            ConfigurationException newEx = new ConfigurationException(Messages.getString("jndi.23"));
            newEx.setRootCause(ex);
            throw newEx;
        }
        URL url = null;
        InputStream is = null;
        final Properties p = new Properties();
        while (e.hasMoreElements()) {
            url = e.nextElement();
            try {
                if (null != (is = url.openStream())) {
                    p.load(is);
                    mergeEnvironment(p, existingProps, true);
                    p.clear();
                }
            } catch (IOException ex) {
                ConfigurationException newEx = new ConfigurationException(Messages.getString("jndi.24"));
                newEx.setRootCause(ex);
                throw newEx;
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException ex) {
                } finally {
                    is = null;
                }
            }
        }
        return existingProps;
    }
