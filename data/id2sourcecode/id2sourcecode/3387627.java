    public void setNodeChannelRef(final NodeChannelRef nodeChannelRef) {
        synchronized (this) {
            _nodeChannelRef = nodeChannelRef;
            setChannel(nodeChannelRef != null ? nodeChannelRef.getChannel() : null);
        }
    }
