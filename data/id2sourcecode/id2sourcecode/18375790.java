    public StripChannelBinding getChannelBinding(String channelName) {
        Iterator<StripChannelBinding> it = channelBindings.iterator();
        while (it.hasNext()) {
            StripChannelBinding channel = it.next();
            if (channel.getBoundChannelName().equals(channelName) || channel.getUserLabel().equals(channelName)) return channel;
        }
        return null;
    }
