    private synchronized void sendContent(Buffer content) {
        if (!waitingResponse.get()) {
            String url = "http://" + auth.domain + "/invoke/push";
            HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url);
            request.setHeader("Host", auth.domain);
            request.setHeader(HttpHeaders.Names.CONNECTION, "keep-alive");
            if (null != C4ClientConfiguration.getInstance().getLocalProxy()) {
                ProxyInfo info = C4ClientConfiguration.getInstance().getLocalProxy();
                if (null != info.user) {
                    String userpass = info.user + ":" + info.passwd;
                    String encode = Base64.encodeToString(userpass.getBytes(), false);
                    request.setHeader(HttpHeaders.Names.PROXY_AUTHORIZATION, "Basic " + encode);
                }
            }
            request.setHeader(HttpHeaders.Names.CONTENT_TRANSFER_ENCODING, HttpHeaders.Values.BINARY);
            request.setHeader("UserToken", ConnectionHelper.getUserToken());
            request.setHeader(HttpHeaders.Names.USER_AGENT, C4ClientConfiguration.getInstance().getUserAgent());
            request.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/octet-stream");
            boolean isPullEnabled = C4ClientConfiguration.getInstance().isClientPullEnable();
            if (isPullEnabled) {
                request.setHeader(HttpHeaders.Names.TRANSFER_ENCODING, "chunked");
                getRemoteFuture().getChannel().write(request);
                waitingResponse.set(true);
                transactionStarted.set(true);
                transactionChunkSize = 0;
                SharedObjectHelper.getGlobalTimer().schedule(this, C4ClientConfiguration.getInstance().getPullTransactionTime(), TimeUnit.SECONDS);
            } else {
                ChannelBuffer sent = ChannelBuffers.wrappedBuffer(content.getRawBuffer(), content.getReadIndex(), content.readableBytes());
                ChannelBuffer sizeBuf = ChannelBuffers.buffer(4);
                sizeBuf.writeInt(sent.readableBytes());
                ChannelBuffer tmp = ChannelBuffers.wrappedBuffer(sizeBuf, sent);
                request.setHeader("Content-Length", String.valueOf(tmp.readableBytes()));
                request.setContent(tmp);
                getRemoteFuture().getChannel().write(request);
                if (logger.isDebugEnabled()) {
                    logger.debug("#############" + request);
                }
                waitingResponse.set(true);
                transactionStarted.set(true);
                return;
            }
        }
        if (null != content) {
            queuedContents.add(content);
        }
        if (transactionStarted.get()) {
            finishQueuedContens();
        }
    }
