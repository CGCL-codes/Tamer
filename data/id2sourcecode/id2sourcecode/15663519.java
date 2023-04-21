    public Set<HGPeerIdentity> getChannelPeers(String channelId) {
        Set<HGPeerIdentity> S = channelPeers.get(channelId);
        if (S == null) return new HashSet<HGPeerIdentity>(); else return S;
    }
