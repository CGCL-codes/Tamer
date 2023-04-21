    public Future<Void> connect(InetSocketAddress host) {
        Future<Void> ret;
        connectLock.lock();
        if (isConnected()) {
            connectLock.unlock();
            throw new IllegalStateException("Already connected!");
        }
        if (connectFuture == null) {
            connectFuture = Futures.newFuturePackage();
            ret = connectFuture.getFuture();
            bootstrap.connect(host).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    connectLock.lock();
                    FutureManager<Void> fm = connectFuture.getManager();
                    connectFuture = null;
                    connectLock.unlock();
                    if (!future.isSuccess()) {
                        fm.cancelFuture(future.getCause());
                    } else {
                        channel = future.getChannel();
                        connected = true;
                        fm.completeFuture(null);
                    }
                }
            });
        } else {
            ret = connectFuture.getFuture();
        }
        connectLock.unlock();
        return ret;
    }
