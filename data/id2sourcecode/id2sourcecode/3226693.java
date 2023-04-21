    public void sessionOpened(IoSession session) {
        session.setIdleTime(IdleStatus.BOTH_IDLE, 0);
        session.setAttribute("CHANNEL_NAME", connectorISO.getChannelName());
        if (logger.isDebugEnabled()) logger.debug(connectorISO.getChannelName() + "| Cliente conectado: " + session.getRemoteAddress() + "|");
    }
