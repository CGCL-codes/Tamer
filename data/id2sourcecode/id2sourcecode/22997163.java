    public static PeerGroupID createPeerGroupID(final PeerGroup parent, final String groupName) throws Exception {
        String seed = groupName + SEED;
        return IDFactory.newPeerGroupID(parent.getPeerGroupID(), Thumbprint.digest(seed.toLowerCase().getBytes(ENCODING)));
    }
