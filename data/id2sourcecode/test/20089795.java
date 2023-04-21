    protected Channel getNonSerializedChannel(Channel serChannel) {
        for (IdentityMediationUnit idu : idauRegistry.getIdentityMediationUnits()) {
            for (Channel c : idu.getChannels()) {
                if (c.getName().equals(serChannel.getName())) return c;
            }
        }
        return null;
    }
