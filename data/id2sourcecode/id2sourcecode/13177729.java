    @Override
    public void handle(TxEvent event) {
        getState().getTxCount().set(count++);
        if (getLog().isTraceEnabled()) {
            getLog().trace(event.getTraceString());
        }
        if (getState().getContext().containsLocalContainer(event.getSource().getIdentity(), event.getSource().getType(), event.getSource().getDomain())) {
            byte[] data = event.getBuffer();
            getChannelOps().send(Channel.createDataMessage(data));
        } else {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Source is destroyed, not sending " + event);
            }
        }
    }
