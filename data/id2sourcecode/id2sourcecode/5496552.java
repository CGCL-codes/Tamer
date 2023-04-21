    protected FederationChannel getFederationChannel(String destAlias) throws SSOException {
        if (channel instanceof FederationChannel) {
            return (FederationChannel) channel;
        }
        FederatedLocalProvider provider = getFederatedProvider();
        FederationChannel fChannel = provider.getChannel();
        for (FederationChannel f : provider.getChannels()) {
            if (f.getMember().getAlias().equals(destAlias)) fChannel = f;
        }
        return fChannel;
    }
