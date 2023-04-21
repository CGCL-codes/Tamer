    public void shutdown() {
        m_messengers.getChannelMessenger().unregisterChannelSubscriber(m_chatChannelSubscribor, new RemoteName(m_chatChannelName, IChatChannel.class));
        if (m_messengers.getMessenger().isConnected()) {
            final RemoteName chatControllerName = ChatController.getChatControlerRemoteName(m_chatName);
            final IChatController controller = (IChatController) m_messengers.getRemoteMessenger().getRemote(chatControllerName);
            controller.leaveChat();
        }
    }
