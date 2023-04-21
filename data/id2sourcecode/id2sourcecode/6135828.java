    protected void initConnectedChannel(boolean handshake, ChannelFuture future, final ChannelFutureListener listener) {
        if (null != proxyHost) {
            if (logger.isDebugEnabled()) {
                logger.debug("Start Send Connect Request!");
            }
            HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.CONNECT, getGoogleHttpsHost() + ":" + 443);
            request.setHeader(HttpHeaders.Names.HOST, getGoogleHttpsHost() + ":443");
            if (null != proxyUser) {
                String userpass = proxyUser + ":" + proxyPass;
                String encode = Base64.encodeToString(userpass.getBytes(), false);
                request.setHeader(HttpHeaders.Names.PROXY_AUTHORIZATION, "Basic " + encode);
            }
            sslProxyConnectionStatus.set(WAITING_CONNECT_RESPONSE);
            future.getChannel().write(request);
            synchronized (sslProxyConnectionStatus) {
                try {
                    sslProxyConnectionStatus.wait(60000);
                    if (sslProxyConnectionStatus.get() != CONNECT_RESPONSED) {
                        closeLocalChannel();
                        remoteChannel = null;
                        return;
                    }
                } catch (InterruptedException e) {
                } finally {
                    sslProxyConnectionStatus.set(INITED);
                }
            }
        }
        remoteChannel = future.getChannel();
        if (!handshake) {
            removeCodecHandler(remoteChannel);
            try {
                listener.operationComplete(null);
            } catch (Exception e) {
            }
            return;
        }
        try {
            SSLContext sslContext = SSLContext.getDefault();
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(true);
            SslHandler sl = null;
            remoteChannel.getPipeline().addFirst("sslHandler", new SslHandler(sslEngine));
            ChannelFuture hf = remoteChannel.getPipeline().get(SslHandler.class).handshake();
            hf.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture fa) throws Exception {
                    if (!fa.isSuccess()) {
                        logger.error("Handshake failed", fa.getCause());
                        closeRemote();
                        closeLocalChannel();
                        return;
                    }
                    removeCodecHandler(remoteChannel);
                    if (logger.isDebugEnabled()) {
                        logger.debug("SSL handshake success!");
                    }
                    listener.operationComplete(null);
                }
            });
        } catch (Exception ex) {
            logger.error(null, ex);
            closeRemote();
        }
    }
