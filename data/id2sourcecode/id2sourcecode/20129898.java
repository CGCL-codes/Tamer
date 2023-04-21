    public ServerSocketChannel getChannel() {
        logger.debug("Entering getChannel().");
        ServerSocketChannel serverSocketChannel = super.getChannel();
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getChannel(); RV = [" + serverSocketChannel + "].");
        }
        return serverSocketChannel;
    }
