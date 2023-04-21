    public void stop() {
        ChannelGroupFuture future = getChannelGroup().close();
        future.awaitUninterruptibly();
        getFactory().releaseExternalResources();
    }
