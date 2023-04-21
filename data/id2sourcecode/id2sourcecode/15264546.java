    public boolean skip(HotspotEvent event) {
        if (!(event instanceof MessageEvent)) {
            return false;
        }
        MessageEvent msgEvent = (MessageEvent) event;
        if (channel == null) {
            return msgEvent.getChannel() == null && messageClass.isInstance(msgEvent.getMessage());
        }
        return channel.equals(msgEvent.getChannel()) && messageClass.isInstance(msgEvent.getMessage());
    }
