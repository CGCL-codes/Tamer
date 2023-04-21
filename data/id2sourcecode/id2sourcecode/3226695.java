    public void sendMessage(IoSession session, IMessage message) {
        if (logger.isDebugEnabled()) logger.debug(connectorISO.getChannelName() + "| Enviando mensaje. |" + message);
        session.write(message);
    }
