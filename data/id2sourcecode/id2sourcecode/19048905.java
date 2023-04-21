    protected ChannelFuture getChannelFuture(HTTPRequestEvent req) {
        ChannelFuture future = null;
        synchronized (externalHostsToChannelFutures) {
            Queue<ChannelFuture> futures = externalHostsToChannelFutures.get(getRemoteAddressFromRequestEvent(req));
            if (futures != null) {
                do {
                    if (futures.isEmpty()) {
                        break;
                    }
                    ChannelFuture cf = futures.remove();
                    if (cf != null && cf.isSuccess() && !cf.getChannel().isConnected()) {
                        continue;
                    }
                    future = cf;
                    break;
                } while (true);
            }
        }
        if (null == future) {
            future = newRemoteChannelFuture(req);
        }
        DirectRemoteChannelResponseHandler handler = future.getChannel().getPipeline().get(DirectRemoteChannelResponseHandler.class);
        handler.relaySession = this;
        currentChannelFuture = future;
        waitingChunkSequence = 0;
        seqChunkTable.clear();
        return future;
    }
