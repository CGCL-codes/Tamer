    private void replaceListeners(ChannelHandlerContext context, HttpRequest request) {
        ChannelPipeline pipeline = context.getChannel().getPipeline();
        pipeline.remove("aggregator");
        int maxFrameSize = engine.getEngineConfiguration().getMaxFrameSize();
        if (hasHeaderWithValue(request, ExtendedHttpHeaders.Names.SEC_WEBSOCKET_VERSION, WebsocketVersions.HYBI_13.getVersionCode())) {
            pipeline.replace("decoder", "wsDecoder", new HybiFrameDecoder(maxFrameSize));
            pipeline.replace("encoder", "wsEncoder", new HybiFrameEncoder());
        } else {
            pipeline.replace("decoder", "wsDecoder", new WebSocketFrameDecoder(maxFrameSize));
            pipeline.replace("encoder", "wsEncoder", new WebSocketFrameEncoder());
        }
    }
