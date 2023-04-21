    @Override
    public void onEvent(EventHeader header, final Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Handler event:" + header.type + " in forward session");
        }
        switch(header.type) {
            case HTTPEventContants.HTTP_REQUEST_EVENT_TYPE:
                {
                    final HTTPRequestEvent request = (HTTPRequestEvent) event;
                    ChannelFuture curFuture = getRemoteFuture();
                    if (curFuture.getChannel().isConnected()) {
                        onRemoteConnected(curFuture, request);
                    } else {
                        ChannelFutureListener listener = new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isSuccess()) {
                                    onRemoteConnected(future, request);
                                } else {
                                    HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE);
                                    localChannel.write(res).addListener(ChannelFutureListener.CLOSE);
                                }
                            }
                        };
                        curFuture.addListener(listener);
                    }
                    break;
                }
            case HTTPEventContants.HTTP_CHUNK_EVENT_TYPE:
                {
                    HTTPChunkEvent chunk = (HTTPChunkEvent) event;
                    final ChannelBuffer buf = ChannelBuffers.wrappedBuffer(chunk.content);
                    if (null != remoteFuture && remoteFuture.getChannel().isConnected()) {
                        remoteFuture.getChannel().write(buf);
                    } else {
                        remoteFuture.addListener(new ChannelFutureListener() {

                            public void operationComplete(final ChannelFuture future) throws Exception {
                                remoteFuture.getChannel().write(buf);
                            }
                        });
                    }
                    break;
                }
            case HTTPEventContants.HTTP_CONNECTION_EVENT_TYPE:
                {
                    HTTPConnectionEvent ev = (HTTPConnectionEvent) event;
                    if (ev.status == HTTPConnectionEvent.CLOSED) {
                        closeRemote();
                    }
                    break;
                }
            default:
                {
                    logger.error("Unexpected event type:" + header.type);
                    break;
                }
        }
    }
