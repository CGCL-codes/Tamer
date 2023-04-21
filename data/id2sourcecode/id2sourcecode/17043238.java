    public StatusManager(final Messengers messengers) {
        m_messengers = messengers;
        m_statusChannelSubscribor = new IStatusChannel() {

            public void statusChanged(final INode node, final String status) {
                synchronized (m_mutex) {
                    if (status == null) {
                        m_status.remove(node);
                    } else {
                        m_status.put(node, status);
                    }
                }
                notifyStatusChanged(node, status);
            }
        };
        if (messengers.getMessenger().isServer() && !messengers.getRemoteMessenger().hasLocalImplementor(IStatusController.STATUS_CONTROLLER)) {
            final StatusController controller = new StatusController(messengers);
            messengers.getRemoteMessenger().registerRemote(controller, IStatusController.STATUS_CONTROLLER);
        }
        m_messengers.getChannelMessenger().registerChannelSubscriber(m_statusChannelSubscribor, IStatusChannel.STATUS_CHANNEL);
        final IStatusController controller = (IStatusController) m_messengers.getRemoteMessenger().getRemote(IStatusController.STATUS_CONTROLLER);
        final Map<INode, String> values = controller.getAllStatus();
        synchronized (m_mutex) {
            m_status.putAll(values);
        }
    }
