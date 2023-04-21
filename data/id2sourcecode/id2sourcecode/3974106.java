    public List<CircleOfTrustMemberDescriptor> getMembers(String configurationKey) {
        List<CircleOfTrustMemberDescriptor> members = new ArrayList<CircleOfTrustMemberDescriptor>();
        FederationService federationSvc = null;
        for (FederationService fc : federationServices) {
            if (fc.getName().equals(configurationKey)) {
                federationSvc = fc;
                break;
            }
        }
        if (federationSvc == null) return members;
        for (FederationChannel channel : federationSvc.getOverrideChannels()) {
            members.add(channel.getMember());
        }
        if (federationSvc.getChannel() != null) members.add(federationSvc.getChannel().getMember());
        return members;
    }
