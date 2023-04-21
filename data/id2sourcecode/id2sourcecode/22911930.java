    private synchronized void updateMetadata(Sink metadataSink) {
        Map<String, Channel> newChannels = new HashMap<String, Channel>();
        try {
            ctree = getChannelTree(metadataSink, newChannels);
        } catch (SAPIException e) {
            log.error("Failed to update metadata: " + e.getMessage() + ".");
            if (!metadataSink.VerifyConnection()) {
                log.error("Metadata RBNB connection is severed, try to reconnect to " + rbnbController.getRBNBConnectionString() + ".");
                metadataSink.CloseRBNBConnection();
                try {
                    metadataSink.OpenRBNBConnection(rbnbController.getRBNBConnectionString(), "RDVMetadata");
                } catch (SAPIException error) {
                    log.error("Failed to connect to RBNB server: " + error.getMessage());
                    error.printStackTrace();
                }
            }
            return;
        }
        channels = newChannels;
        fireMetadataUpdated(ctree);
        notifyAll();
    }
