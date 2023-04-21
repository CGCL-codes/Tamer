    @Override
    public void onEvent(EventHeader header, final Event event) {
        switch(header.type) {
            case HTTPEventContants.HTTP_REQUEST_EVENT_TYPE:
                {
                    final HTTPRequestEvent req = (HTTPRequestEvent) event;
                    ChannelFuture future = getChannelFuture(req);
                    if (future.getChannel().isConnected()) {
                        onRemoteConnected(future, req);
                    } else {
                        future.addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture cf) throws Exception {
                                onRemoteConnected(cf, req);
                            }
                        });
                    }
                    break;
                }
            case HTTPEventContants.HTTP_CHUNK_EVENT_TYPE:
                {
                    HTTPChunkEvent chunk = (HTTPChunkEvent) event;
                    final ChannelBuffer buf = ChannelBuffers.wrappedBuffer(chunk.content);
                    if (currentChannelFuture.getChannel().isConnected()) {
                        currentChannelFuture = currentChannelFuture.getChannel().write(buf);
                    } else {
                        if (currentChannelFuture.isSuccess()) {
                            logger.error("####Session[" + getID() + "] current remote connection already closed, while chunk size:" + buf.readableBytes());
                            closeLocalChannel();
                        } else {
                            currentChannelFuture.addListener(new ChannelFutureListener() {

                                public void operationComplete(final ChannelFuture future) throws Exception {
                                    if (future.isSuccess()) {
                                        future.getChannel().write(buf);
                                    } else {
                                        logger.error("Remote connection closed.");
                                        closeLocalChannel();
                                    }
                                }
                            });
                        }
                    }
                    break;
                }
            case HTTPEventContants.HTTP_CONNECTION_EVENT_TYPE:
                {
                    HTTPConnectionEvent ev = (HTTPConnectionEvent) event;
                    if (ev.status == HTTPConnectionEvent.CLOSED) {
                        if (null != currentChannelFuture && !currentChannelFuture.isDone()) {
                            currentChannelFuture.addListener(new ChannelFutureListener() {

                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    closeRemote();
                                }
                            });
                        } else {
                            closeRemote();
                        }
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
