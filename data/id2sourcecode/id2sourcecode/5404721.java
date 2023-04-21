    @Override
    public void sessionClosed(IoSession session) {
        if (logger.isDebugEnabled()) logger.debug(connectorIFX.getChannelName() + "| Conexi�n cerrada: " + session.getRemoteAddress() + " |");
    }
