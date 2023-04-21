    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_FRAME_BYTES_LENGTH, 0, 4, 0, 4));
        p.addLast("protobufDecoder", new ProtobufDecoder(defaultInstance));
        p.addLast("frameEncoder", new LengthFieldPrepender(4));
        p.addLast("protobufEncoder", new ProtobufEncoder());
        p.addLast("handler", handlerFactory.getChannelUpstreamHandler());
        return p;
    }
