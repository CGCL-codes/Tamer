    public static PeerID createPeerID(String infrastructureseed, String peerName) throws Exception {
        String seed = rand.nextLong() + peerName + SEED;
        return IDFactory.newPeerID(getInfrastructureGroupID(infrastructureseed), Thumbprint.digest(seed.toLowerCase().getBytes(ENCODING)));
    }
