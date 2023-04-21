    protected synchronized void attach() {
        if (chatLogReceiver == null) {
            try {
                ClientSideCommunicationFactory factory = ClientSideCommunicationFactory.getInstance();
                chatView.setLocalUserGroup(ClientApplication.getInstance().getUserManager().getUserGroup());
                rel = new ReplicatedEventListener();
                chatLogReceiver = factory.attachToList(getChannelId(), rel);
                chatView.activate();
            } catch (NetworkException e) {
                ClientXPLog.logException(0, "Chat Controller", "error when trying to attach", e, false);
            }
        }
    }
