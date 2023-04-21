    public void close() {
        LOG.debug("--> close()");
        try {
            if (getState() == STATE_ACTIVE && getChannel().getRequestHandler() != null) {
                getChannel().close();
            }
        } catch (BEEPException be) {
            LOG.warn("could not close channel [" + be.getMessage() + "]");
        }
        setChannel(null);
        setReplyListener(null);
        setState(STATE_CLOSED);
        LOG.debug("<-- close()");
    }
