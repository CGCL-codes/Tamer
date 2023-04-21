    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        if (q instanceof ClientQueue) {
            Handler handler = new Handler();
            bootstrap.setPipelineFactory(new PipelineFactory(handler));
        }
        if (q instanceof ForwarderQueue) {
            ForwarderHandler handler = new ForwarderHandler();
            bootstrap.setPipelineFactory(new PipelineFactory(handler));
        }
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            bootstrap.releaseExternalResources();
            self = null;
            return;
        }
        ChannelFuture lastWriteFuture = null;
        while (self == thisThread) {
            String s = "";
            final ChannelBuffer buf = q.get();
            if (buf.capacity() >= 5) {
                s = buf.toString(0, 5, "utf8");
            }
            if (s.equalsIgnoreCase("<bye>")) {
                break;
            } else {
                buf.readerIndex(0);
            }
            lastWriteFuture = channel.write(buf);
            lastWriteFuture.addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        fireDataSentFromQueue(buf);
                    }
                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            }
            yield();
        }
        if (lastWriteFuture != null) {
            lastWriteFuture.awaitUninterruptibly();
        }
        channel.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
