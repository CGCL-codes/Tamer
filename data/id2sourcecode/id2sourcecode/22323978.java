    protected FederationChannel resolveIdPChannel(FederatedLocalProvider sp, String targetAlias) {
        for (FederationChannel fChannel : sp.getChannels()) {
            FederatedProvider target = fChannel.getTargetProvider();
            for (CircleOfTrustMemberDescriptor member : target.getMembers()) {
                if (member.getAlias().equals(targetAlias)) {
                    if (logger.isTraceEnabled()) logger.trace("Selected IDP Channel " + fChannel.getName());
                    return fChannel;
                }
            }
        }
        if (logger.isTraceEnabled()) logger.trace("Selected default IDP Channel " + sp.getChannel().getName());
        return sp.getChannel();
    }
