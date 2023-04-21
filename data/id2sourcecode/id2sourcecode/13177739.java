    final void doSynAckReceived() {
        if (!getChannelOps().isConnectionSyn()) {
            if (Channel.LOG.isDebugEnabled()) {
                Channel.LOG.debug(getChannel() + " received SYN_ACK, channel is ready.");
            }
            getChannelOps().setConnectionSyn();
            ChannelReadyEvent event = new ChannelReadyEvent(getState().getContext(), getChannel());
            getState().getContext().queueEvent(event);
        } else {
            if (getLog().isDebugEnabled()) {
                getLog().debug(getChannel() + " is already ready, ignoring duplicate SYN_ACK.");
            }
        }
    }
