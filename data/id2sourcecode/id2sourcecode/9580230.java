    protected boolean doSend(Buffer content) {
        waitingResponse.set(true);
        if (cfg.getConnectionMode().equals(ConnectionMode.HTTPS)) {
            remoteAddress.trnasform2Https();
        }
        String url = remoteAddress.toPrintableString();
        if (cfg.isSimpleURLEnable()) {
            if (null == cfg.getLocalProxy() || null == cfg.getLocalProxy().user) {
                url = remoteAddress.getPath();
            }
        }
        final HttpRequest request = buildSentRequest(url, content);
        if (null == clientChannel || !clientChannel.isConnected()) {
            ChannelFuture connFuture = connectRemoteProxyServer(remoteAddress);
            connFuture.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ChannelInitTask result = initConnectedChannel(future.getChannel(), request);
                        result.onVerify();
                    } else {
                        close();
                    }
                }
            });
        } else {
            clientChannel.write(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Send event via connected HTTP connection.");
            }
        }
        return true;
    }
