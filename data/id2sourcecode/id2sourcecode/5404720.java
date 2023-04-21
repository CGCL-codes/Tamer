    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error(connectorIFX.getChannelName() + "| " + cause.getMessage() + " |");
        connectorIFX.stopConnect();
        connectorIFX.tryConnect();
    }
