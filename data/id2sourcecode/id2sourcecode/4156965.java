    void setSelectionKeyToReadImmediately(final AbstractChannelBasedEndpoint handler) {
        SelectionKey key = handler.getChannel().keyFor(selector);
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
    }
