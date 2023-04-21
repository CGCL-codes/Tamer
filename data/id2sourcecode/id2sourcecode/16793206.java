    public void startResponse() {
        logger.debug("#doResponse client.cid:" + getChannelId());
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
        if (server.asyncConnect(this, targetHost, targetPort, connectTimeout)) {
            client.ref();
        }
    }
