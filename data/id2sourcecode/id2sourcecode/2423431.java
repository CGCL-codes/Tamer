    void handleLeaveEvent(LeaveEvent event) {
        final AsteriskQueueImpl queue = getQueueByName(event.getQueue());
        final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());
        if (queue == null) {
            logger.error("Ignored LeaveEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null) {
            logger.error("Ignored LeaveEvent for unknown channel " + event.getChannel());
            return;
        }
        final AsteriskQueueEntryImpl existingQueueEntry = queue.getEntry(event.getChannel());
        if (existingQueueEntry == null) {
            logger.error("Ignored leave event for non existing queue entry in queue " + event.getQueue() + " for channel " + event.getChannel());
            return;
        }
        queue.removeEntry(existingQueueEntry, event.getDateReceived());
    }
