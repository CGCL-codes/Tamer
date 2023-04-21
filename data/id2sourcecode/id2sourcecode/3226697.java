    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error(connectorISO.getChannelName() + "| " + cause.getMessage() + " |");
        connectorISO.stopConnect();
        connectorISO.tryConnect();
    }
