    public static URLConnection openConnection(URL url) throws IOException {
        LOG.debug("Connecting to url: " + url);
        if (PreferencesDao.getInstance().isProxyEnabled()) {
            checkProxyInstance();
            LOG.info("Returning connection with proxy '" + proxyHostInUse + ":" + proxyPortInUse + "'.");
            return url.openConnection(proxy);
        }
        LOG.info("Returning connection without proxy.");
        return url.openConnection();
    }
