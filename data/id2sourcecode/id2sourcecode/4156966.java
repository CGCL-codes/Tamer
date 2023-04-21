    private void setSelectionKeyToWriteImmediate(final AbstractChannelBasedEndpoint handler) {
        SelectionKey key = handler.getChannel().keyFor(selector);
        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
    }
