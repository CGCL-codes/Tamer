    protected Channel getNonSerializedChannel(Channel serChannel) {
        for (IdentityMediationUnit idu : idsuRegistry.getIdentityMediationUnits()) {
            for (Channel c : idu.getChannels()) {
                if (c.getName().equals(serChannel.getName())) return c;
            }
        }
        return null;
    }
