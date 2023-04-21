    public List<CircleOfTrustMemberDescriptor> getMembers() {
        List<CircleOfTrustMemberDescriptor> members = new ArrayList<CircleOfTrustMemberDescriptor>();
        if (defaultFederationService == null) return members;
        for (FederationChannel channel : defaultFederationService.getOverrideChannels()) {
            members.add(channel.getMember());
        }
        if (defaultFederationService.getChannel() != null) members.add(defaultFederationService.getChannel().getMember());
        return members;
    }
