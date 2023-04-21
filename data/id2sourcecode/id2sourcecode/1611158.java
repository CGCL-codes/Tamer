    protected synchronized void doFinishTransaction() {
        if (null != remoteFuture && remoteFuture.getChannel().isConnected() && waitingResponse.get()) {
            if (transactionChunkSize == 0) {
                EventRestRequest ev = new EventRestRequest();
                Buffer buffer = new Buffer();
                ev.encode(buffer);
                sendContent(buffer);
            }
            HttpChunk trailer = new DefaultHttpChunkTrailer();
            remoteFuture.getChannel().write(trailer);
        } else {
        }
        transactionStarted.set(false);
    }
