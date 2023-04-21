    public void messageReceived(IoSession session, Object message) {
        IMessage im = (IMessage) message;
        if (logger.isDebugEnabled()) logger.debug(connectorISO.getChannelName() + "|Mensaje recibido: |" + message);
        connectorISO.messageProcessTM(im);
    }
