    public SelectionKey register(SelectorHandler handler, int ops) throws ClosedChannelException {
        SelectableChannel channel = handler.getChannel();
        return channel.register(selector, ops, handler);
    }
