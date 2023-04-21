    private void handleRegisterSocket(RegisterSocketEvent e) {
        if (e.error) {
            if (e.getErrorCode() == RegisterSocketEvent.RESOURCE_ALREADY_BOUND_ERROR) logger.warn("The requested resource is already available."); else {
                logger.fatal("Impossible to register socket.");
            }
        }
        myAddress = new InetSocketAddress(e.localHost, e.port);
        receivedRSE = true;
        logger.debug("Socket Registered using the address: " + myAddress);
        sendGroupInit(e.getChannel());
    }
