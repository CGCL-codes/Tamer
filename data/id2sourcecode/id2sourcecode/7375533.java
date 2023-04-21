    private void notifyChanellPlayersChanged() {
        final IClientChannel channel = (IClientChannel) m_channelMessenger.getChannelBroadcastor(IClientChannel.CHANNEL_NAME);
        channel.playerListingChanged(getPlayerListingInternal());
    }
