    public List<CircleOfTrustMemberDescriptor> getAllMembers() {
        List<CircleOfTrustMemberDescriptor> members = new ArrayList<CircleOfTrustMemberDescriptor>();
        if (defaultFederationService == null) return members;
        for (FederationChannel channel : defaultFederationService.getOverrideChannels()) {
            members.add(channel.getMember());
        }
        if (defaultFederationService.getChannel() != null) members.add(defaultFederationService.getChannel().getMember());
        for (FederationService svc : federationServices) {
            members.add(svc.getChannel().getMember());
            for (FederationChannel fc : svc.getOverrideChannels()) {
                members.add(fc.getMember());
            }
        }
        return members;
    }
