    public Call(NewChannelEvent newChannelEvent) {
        ANI = newChannelEvent.getCallerIdNum();
        callingChannel = newChannelEvent.getChannel();
        uniqueId = newChannelEvent.getUniqueId();
        initializeInstance();
    }
