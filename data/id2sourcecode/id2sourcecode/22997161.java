    public static PeerGroupID createSubPeerGroupID(String infrastructureseed, PeerGroup parentGroup, String subgroupName) throws Exception {
        String seed = subgroupName + SEED;
        return IDFactory.newPeerGroupID(getInfrastructureGroupID(seed), Thumbprint.digest(seed.toLowerCase().getBytes(ENCODING)));
    }
