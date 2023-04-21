    public HandlerAdapter(Dispatcher dispatcher, ChannelHandler channelHandler, SelectionKey selectionKey, String debugName) {
        super("HandlerAdapter");
        this.dispatcher = dispatcher;
        this.channelHandler = channelHandler;
        this.selectionKey = selectionKey;
        this.debugName = debugName;
        channelReader = channelHandler.getChannelReader();
        channelWriter = channelHandler.getChannelWriter();
        cachedInterestOps = selectionKey.interestOps();
    }
