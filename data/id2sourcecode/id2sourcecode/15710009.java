    Collection<AsteriskChannel> getChannels() {
        Collection<AsteriskChannel> copy;
        synchronized (channels) {
            copy = new ArrayList<AsteriskChannel>(channels.size() + 2);
            for (AsteriskChannel channel : channels) {
                if (channel.getState() != ChannelState.HUNGUP) {
                    copy.add(channel);
                }
            }
        }
        return copy;
    }
