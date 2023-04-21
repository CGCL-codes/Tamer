    public void start(ChannelUpstreamHandler corr) {
        try {
            channelFactory = newClientSocketChannelFactory();
            cb = new ClientBootstrap(channelFactory);
            ProMap commons = ApplicationContext.getApplicationContext().getMapProperty("rpc.protocol.netty.params");
            boolean enablessl = commons.getBoolean("enablessl", false);
            SSLEngine eg = null;
            if (enablessl) {
                eg = buildSSLEngine(true);
            }
            if (eg != null) cb.getPipeline().addFirst("ssl", new SslHandler(eg));
            cb.getPipeline().addLast("decoder", new ObjectDecoder(commons.getInt("maxFramgeLength_", NettyChannelPipelineFactory.maxFramgeLength_)));
            cb.getPipeline().addLast("encoder", new ObjectEncoder(commons.getInt("estimatedLength_", NettyChannelPipelineFactory.estimatedLength_)));
            cb.getPipeline().addLast("handler", corr);
            cb.setOption("connectTimeoutMillis", commons.getInt("connection.timeout", 10) * 1000);
            ChannelFuture ccf = cb.connect(new InetSocketAddress(host, port));
            boolean success = ccf.awaitUninterruptibly().isSuccess();
            if (!success) {
                throw new NettyRunException("can not connect to:" + host + ":" + port);
            }
            cc = ccf.getChannel();
        } catch (Exception e) {
            this.disconnect();
        }
    }
