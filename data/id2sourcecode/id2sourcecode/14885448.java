    public void registerKeys() {
        super.registerKeys();
        try {
            connectKey = getChannel().register(getSelector(), SelectionKey.OP_CONNECT);
            connectKey.attach(this);
        } catch (ClosedChannelException e) {
            logger.warning(e.toString());
        }
    }
