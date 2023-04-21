    public static PeerGroupID getInfrastructureGroupID(String infrastructureseed) throws Exception {
        return IDFactory.newPeerGroupID(Thumbprint.digest(infrastructureseed.toLowerCase().getBytes(ENCODING)));
    }
