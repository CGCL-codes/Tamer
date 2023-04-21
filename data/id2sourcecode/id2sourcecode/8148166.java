    private void init() {
        final IChatController controller = (IChatController) m_messengers.getRemoteMessenger().getRemote(ChatController.getChatControlerRemoteName(m_chatName));
        m_messengers.getChannelMessenger().registerChannelSubscriber(m_chatChannelSubscribor, new RemoteName(m_chatChannelName, IChatChannel.class));
        final Tuple<List<INode>, Long> init = controller.joinChat();
        synchronized (m_mutex) {
            m_chatInitVersion = init.getSecond().longValue();
            m_nodes = init.getFirst();
            final IModeratorController moderatorController = (IModeratorController) m_messengers.getRemoteMessenger().getRemote(ModeratorController.getModeratorControllerName());
            if (moderatorController != null) {
                for (final INode node : m_nodes) {
                    final boolean admin = moderatorController.isPlayerAdmin(node);
                    if (admin) addToNotesMap(node, "[Mod]");
                }
            }
            for (final Runnable job : m_queuedInitMessages) {
                job.run();
            }
            m_queuedInitMessages = null;
        }
        updateConnections();
    }
