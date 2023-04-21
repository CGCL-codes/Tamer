    protected final void sendMessageAsyncIfNecessary(final ChannelNotificationInfo info) throws MonitorException {
        final ChannelMedia media = info.getChannelMedia();
        if (media.equals(ChannelMedia.ASYNC)) {
            sendMessage(info);
        } else {
            service.execute(new Runnable() {

                public void run() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invoking asynchronously channel notification information [" + info + "]");
                    }
                    sendMessage(info);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invoked asynchronously [" + info + "]");
                    }
                }
            });
        }
    }
