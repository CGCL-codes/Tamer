    private IChatChannel getChatBroadcaster() {
        final IChatChannel chatter = (IChatChannel) m_channelMessenger.getChannelBroadcastor(new RemoteName(m_chatChannel, IChatChannel.class));
        return chatter;
    }
