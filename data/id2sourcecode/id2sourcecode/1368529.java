    public synchronized void registerChannel(SelectableChannel channel, ChannelHandler channelHandler, int interestOps) throws ClosedChannelException {
        selector.wakeup();
        SelectionKey key = channel.register(selector, interestOps);
        HandlerAdapter handlerAdapter = new HandlerAdapter(this, channelHandler, key, channelHandler.toString());
        key.attach(handlerAdapter);
        channelHandler.getChannelReader().setChannel(channel);
        if (channel instanceof WritableByteChannel) {
            WritableByteChannel writableByteChannel = (WritableByteChannel) channel;
            ChannelWriter channelWriter = channelHandler.getChannelWriter();
            channelWriter.setChannel(writableByteChannel);
            channelWriter.setHandlerAdapter(handlerAdapter);
        }
        channelHandler.channelRegistered(handlerAdapter);
    }
