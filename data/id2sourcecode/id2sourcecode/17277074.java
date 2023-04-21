    protected void doDisconnect(String name) throws ConnectionManagerException {
        DatagramChannel dc = null;
        Initiator initiator = container.getInitiator(name);
        dc = (DatagramChannel) initiator.getChannel();
        try {
            if (dc != null) {
                dc.socket().close();
                dc.close();
                dc = null;
            }
            doCloseAll(name);
        } catch (IOException ex) {
            throw new ConnectionManagerException(ex);
        }
    }
