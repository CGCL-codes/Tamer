    public void receiveNUL(Message message) throws AbortChannelException {
        Object appData = message.getChannel().getAppData();
        TCPSession session = (TCPSession) message.getChannel().getSession();
        LOG.debug("received confirmation from [" + appData + ", " + session.getSocket() + "]");
    }
