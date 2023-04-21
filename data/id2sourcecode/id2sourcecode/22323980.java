    protected FederationChannel resolveSpChannel(CircleOfTrustMemberDescriptor spDescriptor) {
        ClaimChannel cChannel = (ClaimChannel) channel;
        FederatedLocalProvider idp = cChannel.getProvider();
        FederationChannel spChannel = idp.getChannel();
        for (FederationChannel fChannel : idp.getChannels()) {
            FederatedProvider sp = fChannel.getTargetProvider();
            for (CircleOfTrustMemberDescriptor member : sp.getMembers()) {
                if (member.getAlias().equals(spDescriptor.getAlias())) {
                    if (logger.isDebugEnabled()) logger.debug("Selected IdP channel " + fChannel.getName() + " for provider " + sp.getName());
                    spChannel = fChannel;
                    break;
                }
            }
        }
        return spChannel;
    }
