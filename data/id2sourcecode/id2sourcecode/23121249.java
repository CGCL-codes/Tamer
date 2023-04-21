    protected ChannelPipelineFactory getChannelPipelineFactory() {
        return new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
                pipeline.addLast("handler", handler);
                return pipeline;
            }
        };
    }
