    public void sendSlap(final String playerName) {
        final IChatChannel remote = (IChatChannel) m_messengers.getChannelMessenger().getChannelBroadcastor(new RemoteName(m_chatChannelName, IChatChannel.class));
        remote.slapOccured(playerName);
    }
