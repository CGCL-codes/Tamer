    protected URLConnection openConnection() throws IOException {
        try {
            Proxy proxy = WWIO.configureProxy();
            if (proxy != null) this.connection = this.url.openConnection(proxy); else this.connection = this.url.openConnection();
        } catch (java.io.IOException e) {
            Logging.logger().log(Level.SEVERE, Logging.getMessage("URLRetriever.ErrorOpeningConnection", this.url.toString()), e);
            throw e;
        }
        if (this.connection == null) {
            String message = Logging.getMessage("URLRetriever.NullReturnedFromOpenConnection", this.url);
            Logging.logger().severe(message);
            throw new IllegalStateException(message);
        }
        this.connection.setConnectTimeout(this.connectTimeout);
        this.connection.setReadTimeout(this.readTimeout);
        return connection;
    }
