    @Override
    public void handle(ContainerDestroyedEvent event) {
        if (getState().getTxSubscriptionManager().includes(new SubscriptionParameters(event.getIdentity(), event.getType(), event.getDomain()))) {
            getChannelOps().send(getChannelOps().getContainerDestroyedMessage(event));
        }
    }
