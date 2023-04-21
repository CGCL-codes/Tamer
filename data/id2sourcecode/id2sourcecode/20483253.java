    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        final DNSMessage original = DNSMessage.class.cast(e.getMessage());
        ClientBootstrap cb = new ClientBootstrap(this.clientChannelFactory);
        cb.setOption("broadcast", "false");
        cb.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ClientHanler(original, e.getChannel(), e.getRemoteAddress()));
            }
        });
        List<SocketAddress> newlist = new ArrayList<SocketAddress>(this.config.getForwarders());
        sendRequest(e, original, cb, newlist);
    }
