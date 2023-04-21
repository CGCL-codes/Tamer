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
            case C4Constants.EVENT_SEQUNCEIAL_CHUNK_TYPE:
                {
                    SequentialChunkEvent sequnce = (SequentialChunkEvent) event;
                    ChannelBuffer sent = null;
                    synchronized (seqChunkTable) {
                        seqChunkTable.put(sequnce.sequence, sequnce);
                        SequentialChunkEvent chunk = seqChunkTable.remove(waitingChunkSequence);
                        while (null != chunk) {
                            waitingChunkSequence++;
                            ChannelBuffer buf = ChannelBuffers.wrappedBuffer(chunk.content);
                            if (null == sent) {
                                sent = buf;
                            } else {
                                sent = ChannelBuffers.wrappedBuffer(sent, buf);
                            }
                            chunk = seqChunkTable.remove(waitingChunkSequence);
                        }
                        if (null != sent) {
                            sendContent(sent);
                        }
                    }
                    break;
                }
            case HTTPEventContants.HTTP_CHUNK_EVENT_TYPE:
                {
                    HTTPChunkEvent chunk = (HTTPChunkEvent) event;
                    ChannelBuffer buf = ChannelBuffers.wrappedBuffer(chunk.content);
                    sendContent(buf);
                    break;
                }
            case HTTPEventContants.HTTP_CONNECTION_EVENT_TYPE:
                {
                    logger.info("Session[" + event.getHash() + "] handle connection event.");
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
