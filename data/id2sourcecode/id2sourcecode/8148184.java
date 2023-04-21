        public void slapOccured(final String to) {
            final INode from = MessageContext.getSender();
            if (isIgnored(from)) {
                return;
            }
            synchronized (m_mutex) {
                if (to.equals(m_messengers.getChannelMessenger().getLocalNode().getName())) {
                    for (final IChatListener listener : m_listeners) {
                        final String message = "You were slapped by " + from.getName();
                        m_chatHistory.add(new ChatMessage(message, from.getName(), false));
                        listener.addMessage(message, from.getName(), false);
                    }
                } else if (from.equals(m_messengers.getChannelMessenger().getLocalNode())) {
                    for (final IChatListener listener : m_listeners) {
                        final String message = "You just slapped " + to;
                        m_chatHistory.add(new ChatMessage(message, from.getName(), false));
                        listener.addMessage(message, from.getName(), false);
                    }
                }
            }
        }
