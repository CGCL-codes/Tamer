    final void doSynReceived() {
        if (Channel.LOG.isDebugEnabled()) {
            Channel.LOG.debug(getChannel() + " received SYN, sending SYN_ACK");
        }
        getChannelOps().send(Channel.MSG_SYN_ACK_BYTES);
        if (!getChannelOps().isConnectionSyn()) {
            if (Channel.LOG.isDebugEnabled()) {
                Channel.LOG.debug(getChannel() + " re-sending SYN");
            }
            getChannelOps().send(Channel.MSG_SYN_BYTES);
        }
    }
