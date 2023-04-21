    void sendMessage(final String message, final boolean meMessage) {
        final IChatChannel remote = (IChatChannel) m_messengers.getChannelMessenger().getChannelBroadcastor(new RemoteName(m_chatChannelName, IChatChannel.class));
        if (meMessage) remote.meMessageOccured(message); else remote.chatOccured(message);
        m_sentMessages.append(message);
    }
