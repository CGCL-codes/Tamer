    public void sessionIdle(IoSession session, IdleStatus status) {
        if (logger.isDebugEnabled()) logger.debug(connectorISO.getChannelName() + "| Conexi�n terminada por exceder el tiempo de espera sin actividad.|");
        session.close();
    }
