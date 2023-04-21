    protected SPChannel resolveSpChannel(CircleOfTrustMemberDescriptor idp) throws SSOException {
        CircleOfTrust cot = getProvider().getChannel().getCircleOfTrust();
        SPChannel spChannel = null;
        for (Provider p : cot.getProviders()) {
            if (p instanceof FederatedLocalProvider) {
                FederatedLocalProvider lp = (FederatedLocalProvider) p;
                if (lp.getChannel() == null || lp.getChannel().getMember() == null) continue;
                if (lp.getChannel().getMember().getAlias().equals(idp.getAlias())) {
                    spChannel = (SPChannel) lp.getChannel();
                    break;
                }
                for (FederationChannel c : lp.getChannels()) {
                    if (c.getMember().getAlias().equals(idp.getAlias())) {
                        spChannel = (SPChannel) c;
                        break;
                    }
                }
                if (spChannel != null) break;
            }
        }
        if (spChannel == null) {
            logger.debug("No SP Channel defined in local providers for " + idp.getAlias());
        }
        return spChannel;
    }
