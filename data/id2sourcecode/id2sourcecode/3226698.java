    @Override
    public void sessionClosed(IoSession session) {
        if (logger.isDebugEnabled()) logger.debug(connectorISO.getChannelName() + "| Conexi�n cerrada: " + session.getRemoteAddress() + " |");
    }
