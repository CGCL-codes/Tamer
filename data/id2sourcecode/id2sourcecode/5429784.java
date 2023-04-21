    private void handleMulticastInit(MulticastInitEvent event) {
        if (event.error) {
            logger.warn("Impossible to register multicast address. Using Point to Point");
        }
        sendGroupInit(event.getChannel());
    }
