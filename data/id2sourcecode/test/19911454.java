    public Collection<CircleOfTrustMemberDescriptor> lookupMembersForProvider(Provider provider, String role) throws CircleOfTrustManagerException {
        Set<CircleOfTrustMemberDescriptor> members = new HashSet<CircleOfTrustMemberDescriptor>();
        for (Provider destProvider : cot.getProviders()) {
            if (destProvider.getRole().equals(role)) {
                if (logger.isDebugEnabled()) logger.debug("Provider " + destProvider.getName() + " has role " + role);
                if (destProvider instanceof FederatedLocalProvider) {
                    FederatedLocalProvider federatedDestProvider = (FederatedLocalProvider) destProvider;
                    members.add(federatedDestProvider.getChannel().getMember());
                    for (FederationChannel channel : federatedDestProvider.getChannels()) {
                        if (channel.getTargetProvider().equals(provider)) {
                            members.add(channel.getMember());
                            if (logger.isDebugEnabled()) logger.debug("Selected targeting member : " + channel.getMember().getAlias());
                        }
                    }
                } else {
                    FederatedRemoteProvider destRemoteProvider = (FederatedRemoteProvider) destProvider;
                    for (CircleOfTrustMemberDescriptor m : destRemoteProvider.getAllMembers()) {
                        if (logger.isDebugEnabled()) logger.debug("Selected member : " + m.getAlias());
                        members.add(m);
                    }
                }
            }
        }
        return members;
    }
