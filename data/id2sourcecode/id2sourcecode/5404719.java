    public void sessionIdle(IoSession session, IdleStatus status) {
        if (logger.isDebugEnabled()) logger.debug(connectorIFX.getChannelName() + "| Conexi�n terminada por exceder el tiempo de espera sin actividad.|");
        session.close();
    }
