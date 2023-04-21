    @Override
    public void handle(ContainerStateChangeEvent event) {
        if (getState().getTxSubscriptionManager().isSubscribed(event.getContainer())) {
            getChannelOps().send(Channel.createDataMessage(getState().getContext().getFrameWriter().writeMeta(event.getContainer())));
        }
    }
