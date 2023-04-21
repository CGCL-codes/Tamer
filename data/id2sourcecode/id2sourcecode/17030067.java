    public boolean connect() {
        ChannelFuture future = bootstrapLocalExec.connect(address);
        channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            logger.error("Client Not Connected", future.getCause());
            return false;
        }
        return true;
    }
