    public void startResponse() {
        logger.debug("#doResponse.id:" + getChannelId());
        this.client = this;
        HeaderParser requestHeader = getRequestHeader();
        ServerParser sslServer = requestHeader.getServer();
        String targetHost = sslServer.getHost();
        int targetPort = sslServer.getPort();
        ServerParser parser = config.findProxyServer(true, targetHost);
        this.isUseProxy = false;
        if (parser != null) {
            this.isUseProxy = true;
            targetHost = parser.getHost();
            targetPort = parser.getPort();
        }
        isConnected = false;
        server.asyncConnect(this, targetHost, targetPort, writeTimeout);
    }
