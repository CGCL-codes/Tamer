    public void run() {
        if (!connection.isConnected()) {
            try {
                logger.debug("CONNECTING...");
                connection.connect();
                connection.addConnectionListener(this);
                connection.login(OldJavverProperties.USERNAME, OldJavverProperties.PASSWORD);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("DISCONNECTING");
            connection.disconnect();
        }
    }
