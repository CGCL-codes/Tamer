    private ChannelImpl getChannel() {
        if (channelRef != null) {
            try {
                return channelRef.get();
            } catch (ObjectNotFoundException e) {
            }
        }
        throw new IllegalStateException("channel is closed");
    }
