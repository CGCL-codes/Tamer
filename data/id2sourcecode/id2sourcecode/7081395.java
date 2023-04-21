    public void setStatus(final String newStatus) {
        final INode node = MessageContext.getSender();
        synchronized (m_mutex) {
            m_status.put(node, newStatus);
        }
        final IStatusChannel channel = (IStatusChannel) m_messengers.getChannelMessenger().getChannelBroadcastor(IStatusChannel.STATUS_CHANNEL);
        channel.statusChanged(node, newStatus);
    }
