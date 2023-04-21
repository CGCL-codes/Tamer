    public Channel getChannel(String aChannelId) {
        if (mSystemChannels.containsKey(aChannelId)) {
            return mSystemChannels.get(aChannelId);
        }
        if (mPrivateChannels.containsKey(aChannelId)) {
            return mPrivateChannels.get(aChannelId);
        }
        if (mPublicChannels.containsKey(aChannelId)) {
            return mPublicChannels.get(aChannelId);
        }
        if (usePersistentStore) {
            Channel channel = mChannelStore.getChannel(aChannelId);
            if (channel != null) {
                mPublicChannels.put(aChannelId, channel);
            }
            return channel;
        }
        return null;
    }
