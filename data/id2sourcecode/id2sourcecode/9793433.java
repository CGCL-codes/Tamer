    public void registerKeys() {
        try {
            readKey = getChannel().register(getSelector(), SelectionKey.OP_READ);
            readKey.attach(this);
            writeKey = getChannel().register(getSelector(), SelectionKey.OP_WRITE);
            writeKey.attach(this);
        } catch (ClosedChannelException e) {
            logger.warning(e.toString());
        }
    }
