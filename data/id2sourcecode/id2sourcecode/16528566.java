    public static final InputStream getConfigStream(final String path) throws HibernateException {
        final URL url = ConfigHelper.locateConfig(path);
        if (url == null) {
            String msg = "Unable to locate config file: " + path;
            log.error(msg);
            throw new HibernateException(msg);
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new HibernateException("Unable to open config file: " + path, e);
        }
    }
