    public void add(final ChannelEvent value) {
        for (final EventStoreListener listener : listenerList) {
            listener.EventTriggered(value.getChannel(), value.getData());
        }
    }
