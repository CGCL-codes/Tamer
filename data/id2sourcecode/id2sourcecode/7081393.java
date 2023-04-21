    protected void connectionRemoved(final INode to) {
        synchronized (m_mutex) {
            m_status.remove(to);
        }
        final IStatusChannel channel = (IStatusChannel) m_messengers.getChannelMessenger().getChannelBroadcastor(IStatusChannel.STATUS_CHANNEL);
        channel.statusChanged(to, null);
    }
