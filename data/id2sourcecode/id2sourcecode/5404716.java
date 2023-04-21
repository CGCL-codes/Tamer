    public void sessionOpened(IoSession session) {
        session.setAttribute("CHANNEL_NAME", connectorIFX.getChannelName());
        if (logger.isDebugEnabled()) logger.debug(connectorIFX.getChannelName() + "| Cliente conectado: " + session.getRemoteAddress() + "|");
    }
