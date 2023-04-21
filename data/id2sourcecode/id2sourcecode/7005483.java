    @Override
    public HessianConnection open(URL url) throws IOException {
        if (log.isLoggable(Level.FINER)) {
            log.finer(this + " open(" + url + ")");
        }
        URLConnection conn = url.openConnection();
        long connectTimeout = _proxyFactory.getConnectTimeout();
        if (connectTimeout >= 0) {
            conn.setConnectTimeout((int) connectTimeout);
        }
        conn.setDoOutput(true);
        long readTimeout = _proxyFactory.getReadTimeout();
        if (readTimeout > 0) {
            try {
                conn.setReadTimeout((int) readTimeout);
            } catch (Throwable e) {
            }
        }
        return new HessianURLConnection(url, conn);
    }
