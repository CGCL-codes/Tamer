    public void receivedMessage(MessageEvent msgEvent) {
        logger.debug("receive MessageEvent for Channel: " + msgEvent.getChannelName());
        processCachedWorkItemsTask(msgEvent.getChannelName());
    }
