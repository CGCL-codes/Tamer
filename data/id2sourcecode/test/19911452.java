    public CircleOfTrustMemberDescriptor lookupMemberForProvider(Provider srcProvider, Provider destProvider) throws CircleOfTrustManagerException {
        CircleOfTrustMemberDescriptor targetingMember = null;
        if (destProvider instanceof FederatedLocalProvider) {
            FederatedLocalProvider federatedDestProvider = (FederatedLocalProvider) destProvider;
            for (FederationChannel channel : federatedDestProvider.getChannels()) {
                if (channel.getTargetProvider().equals(srcProvider)) {
                    targetingMember = channel.getMember();
                    if (logger.isDebugEnabled()) logger.debug("Selected targeting member : " + targetingMember.getAlias());
                }
            }
        }
        if (targetingMember == null) {
            logger.debug("No Selected between source " + srcProvider.getName() + " and destination " + destProvider.getName());
        }
        return targetingMember;
    }
