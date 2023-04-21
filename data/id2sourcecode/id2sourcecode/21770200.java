    public Channel waitForOpenedDataChannel() throws InterruptedException {
        Channel channel = null;
        if (waitForOpenedDataChannel.await(session.getConfiguration().TIMEOUTCON + 1000, TimeUnit.MILLISECONDS)) {
            if (waitForOpenedDataChannel.isSuccess()) {
                channel = waitForOpenedDataChannel.getChannel();
            } else {
                logger.warn("data connection is in error");
            }
        } else {
            logger.warn("Timeout occurs during data connection");
        }
        waitForOpenedDataChannel = new GgChannelFuture(true);
        return channel;
    }
