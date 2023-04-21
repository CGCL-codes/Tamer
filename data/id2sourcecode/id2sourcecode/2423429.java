    private void handleQueueEntryEvent(QueueEntryEvent event) {
        final AsteriskQueueImpl queue = getQueueByName(event.getQueue());
        final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());
        if (queue == null) {
            logger.error("Ignored QueueEntryEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null) {
            logger.error("Ignored QueueEntryEvent for unknown channel " + event.getChannel());
            return;
        }
        if (queue.getEntry(event.getChannel()) != null) {
            logger.error("Ignored duplicate queue entry during population in queue " + event.getQueue() + " for channel " + event.getChannel());
            return;
        }
        int reportedPosition = event.getPosition();
        queue.createNewEntry(channel, reportedPosition, event.getDateReceived());
    }
