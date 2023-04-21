    public void performUpdates() {
        logger.debug("Starting channel updates loop for " + mgr.getChannelGroup().getTitle());
        mgr.notifyPolling(true);
        Iterator iter = mgr.channelIterator();
        Channel nextChan;
        while (iter.hasNext()) {
            nextChan = (Channel) iter.next();
            logger.info("processing: " + nextChan);
            try {
                handleChannel(nextChan, getUpdChanInfo(nextChan));
            } catch (RuntimeException e) {
                logger.error("Error during processing: " + nextChan, e);
            } catch (NoSuchMethodError ignoreNoSuchMethod) {
                logger.error("NoSuchMethodError exception within Run method. Ignoring." + nextChan, ignoreNoSuchMethod);
            }
        }
        mgr.notifyPolling(false);
        mgr.incrPollingCounter();
    }
