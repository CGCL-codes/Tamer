    @Override
    public void handleEvent(final HttpProxyEvent evt, HttpProxyEventCallback callback) {
        if (logger.isDebugEnabled()) {
            logger.debug("Handle event:" + evt.getType());
        }
        final HttpProxyEvent event = preProcessForwardHttpProxyEvent(evt);
        switch(event.getType()) {
            case RECV_HTTP_REQUEST:
            case RECV_HTTPS_REQUEST:
                {
                    localChannel = event.getChannel();
                    final HttpRequest request = (HttpRequest) event.getSource();
                    if (event.getType().equals(HttpProxyEventType.RECV_HTTPS_REQUEST)) {
                        isHttps = true;
                    } else {
                        if (forceShortConnection()) {
                            request.setHeader(HttpHeaders.Names.CONNECTION, "close");
                        }
                    }
                    try {
                        getRemoteChannel(event, new CallBack() {

                            @Override
                            public void callback(Channel remote) throws Exception {
                                remoteChannel = remote;
                                if (null == remoteChannel) {
                                    close();
                                    return;
                                }
                                initCodecHandler(remoteChannel);
                                if (!isHttps || needForwardConnect()) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Send proxy request");
                                        logger.debug(request.toString());
                                    }
                                    remoteChannel.write(request);
                                } else if (isHttps) {
                                    HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                    ChannelFuture future = event.getChannel().write(res);
                                    removeCodecHandler(future);
                                }
                            }
                        });
                    } catch (Exception e) {
                        logger.error("Failed to create remote channel!", e);
                        closeChannel(localChannel);
                    }
                    break;
                }
            case RECV_HTTP_CHUNK:
            case RECV_HTTPS_CHUNK:
                {
                    remoteChannel.write(event.getSource());
                    break;
                }
        }
    }
