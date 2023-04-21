    public void requestRetransmit(String contextIdentity, String identityRegularExpression, IType type, IDomain domain) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("requestRetransmit for context=" + contextIdentity + ", identity=" + identityRegularExpression + ", type=" + type + ", domain=" + domain);
        }
        final IChannel channel = getChannel(contextIdentity);
        if (channel != null) {
            channel.requestRetransmit(identityRegularExpression, type, domain);
        }
    }
