    public void startup() throws Throwable {
        ChannelFactory cf = newClientSocketChannelFactory(executor);
        ClientBootstrap cb = new ClientBootstrap(cf);
        EchoHandler ch = new EchoHandler();
        cb.getPipeline().addLast("decoder", new ObjectDecoder());
        cb.getPipeline().addLast("encoder", new ObjectEncoder());
        cb.getPipeline().addLast("handler", ch);
        ChannelFuture ccf = cb.connect(new InetSocketAddress(NettyUtil.getLocalHost(), 3344));
        assertTrue(ccf.awaitUninterruptibly().isSuccess());
        final Channel cc = ccf.getChannel();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(new Runnable() {

                public void run() {
                    cc.write("hello");
                    TestObject o = new TestObject();
                    o.setId("1");
                    o.setName("duoduo");
                    cc.write(o);
                }
            });
        }
        if (ch.exception.get() != null && !(ch.exception.get() instanceof IOException)) {
            throw ch.exception.get();
        }
        if (ch.exception.get() != null) {
            throw ch.exception.get();
        }
    }
