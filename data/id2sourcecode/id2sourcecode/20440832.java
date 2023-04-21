    private URLConnection connect() throws IOException {
        URL url = new URL(buildUri());
        if (config != null && config.isProxyEnabled()) {
            Proxy proxy = new Proxy(config.getProxyScheme(), new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
            return url.openConnection(proxy);
        }
        return url.openConnection();
    }
