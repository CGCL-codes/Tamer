    @Override
    protected URLConnection openConnection() throws Exception {
        if (!proxyHost.isNull()) {
            return ((URL) url.getValue()).openConnection(getProxy());
        }
        return super.openConnection();
    }
