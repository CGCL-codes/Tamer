    public void channelMessage(final BigInteger channelId, final ByteBuffer message, RequestCompletionHandler<Void> completionHandler) {
        RequestCompletionFuture future = new RequestCompletionFuture(completionHandler);
        if (!readyForRequests(future)) {
            return;
        }
        taskQueue.addTask(new AbstractKernelRunnable("HandleChannelMessage") {

            public void run() {
                ClientSessionImpl sessionImpl = ClientSessionImpl.getSession(dataService, sessionRefId);
                if (sessionImpl != null) {
                    if (isConnected()) {
                        sessionService.getChannelService().handleChannelMessage(channelId, sessionImpl.getWrappedClientSession(), message.asReadOnlyBuffer());
                    }
                } else {
                    scheduleHandleDisconnect(false, true);
                }
            }
        }, identity);
        enqueueCompletionNotification(future);
    }
