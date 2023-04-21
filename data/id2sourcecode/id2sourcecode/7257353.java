    public void retransmitToAll(String identityRegularExpression, IType type, IDomain domain) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("retransmitToAll for identity=" + identityRegularExpression + ", type=" + type + ", domain=" + domain);
        }
        for (IChannel channel : getState().getChannels().values()) {
            try {
                channel.retransmit(identityRegularExpression, type, domain);
            } catch (Exception e) {
                logException(getLog(), channel, e);
            }
        }
    }
