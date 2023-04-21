    private void handleHttpRequest(ChannelHandlerContext aCtx, HttpRequest aReq) {
        if (aReq.getMethod() != HttpMethod.GET) {
            sendHttpResponse(aCtx, aReq, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }
        if (HttpHeaders.Values.UPGRADE.equalsIgnoreCase(aReq.getHeader(HttpHeaders.Names.CONNECTION)) && HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(aReq.getHeader(HttpHeaders.Names.UPGRADE))) {
            HttpResponse lResp = null;
            try {
                lResp = constructHandShakeResponse(aReq, aCtx);
            } catch (NoSuchAlgorithmException lNSAEx) {
                mLog.debug("Channel is disconnected");
                aCtx.getChannel().close();
            }
            aCtx.getChannel().write(lResp);
            mChannels.add(aCtx.getChannel());
            ChannelPipeline lPipeline = aCtx.getChannel().getPipeline();
            lPipeline.remove("aggregator");
            EngineConfiguration lConfig = mEngine.getConfiguration();
            if (lConfig == null || lConfig.getMaxFramesize() == 0) {
                lPipeline.replace("decoder", "jwsdecoder", new WebSocketFrameDecoder(JWebSocketCommonConstants.DEFAULT_MAX_FRAME_SIZE));
            } else {
                lPipeline.replace("decoder", "jwsdecoder", new WebSocketFrameDecoder(lConfig.getMaxFramesize()));
            }
            lPipeline.replace("encoder", "jwsencoder", new WebSocketFrameEncoder());
            if (aReq.getUri().startsWith("wss:")) {
                final SslHandler sslHandler = aCtx.getPipeline().get(SslHandler.class);
                ChannelFuture lHandshakeFuture = sslHandler.handshake();
                lHandshakeFuture.addListener(new SecureWebSocketConnectionListener(sslHandler));
            }
            mConnector = initializeConnector(aCtx, aReq);
            return;
        }
        sendHttpResponse(aCtx, aReq, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
    }
