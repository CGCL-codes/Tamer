    public void retransmit(String contextIdentity, String identityRegularExpression, IType type, IDomain domain) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("retransmit for context=" + contextIdentity + ", identity=" + identityRegularExpression + ", type=" + type + ", domain=" + domain);
        }
        final IChannel channel = getChannel(contextIdentity);
        if (channel != null) {
            channel.retransmit(identityRegularExpression, type, domain);
        }
    }
