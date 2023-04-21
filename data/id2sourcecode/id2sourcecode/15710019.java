    void handleNewExtenEvent(NewExtenEvent event) {
        AsteriskChannelImpl channel;
        final Extension extension;
        channel = getChannelImplById(event.getUniqueId());
        if (channel == null) {
            logger.error("Ignored NewExtenEvent for unknown channel " + event.getChannel());
            return;
        }
        extension = new Extension(event.getContext(), event.getExtension(), event.getPriority(), event.getApplication(), event.getAppData());
        synchronized (channel) {
            channel.extensionVisited(event.getDateReceived(), extension);
        }
    }
