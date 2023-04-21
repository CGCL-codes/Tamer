    protected boolean doIsConnected(String name) {
        DatagramChannel dc = null;
        Initiator initiator = container.getInitiator(name);
        dc = (DatagramChannel) initiator.getChannel();
        if (dc == null) {
            return false;
        } else {
            return dc.socket().isConnected();
        }
    }
