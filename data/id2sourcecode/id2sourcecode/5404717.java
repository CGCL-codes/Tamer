    public void messageReceived(IoSession session, Object message) {
        IMessage im = (IMessage) message;
        if (logger.isDebugEnabled()) logger.debug(connectorIFX.getChannelName() + "|Mensaje recibido: |" + message);
        connectorIFX.messageProcessTM(im);
    }
