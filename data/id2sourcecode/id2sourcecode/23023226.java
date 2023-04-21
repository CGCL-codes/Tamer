    public void clusterDestination(Destination destination) {
        String clusterId = destination.getNetworkSettings().getClusterId();
        if (clusterId == null) clusterId = getDefaultClusterId();
        ClusterSettings cls = clusterSettings.get(clusterId);
        if (cls == null) {
            ClusterException ce = new ClusterException();
            ce.setMessage(10217, new Object[] { destination.getId(), clusterId });
            throw ce;
        }
        for (String channelId : destination.getChannels()) {
            Endpoint endpoint = broker.getEndpoint(channelId);
            String endpointUrl = endpoint.getUrl();
            int endpointPort = endpoint.getPort();
            if (cls.getURLLoadBalancing()) {
                int tokenStart = endpointUrl.indexOf('{');
                if (tokenStart != -1) {
                    int tokenEnd = endpointUrl.indexOf('}', tokenStart);
                    if (tokenEnd == -1) tokenEnd = endpointUrl.length(); else tokenEnd++;
                    ClusterException ce = new ClusterException();
                    ce.setMessage(10209, new Object[] { destination.getId(), channelId, endpointUrl.substring(tokenStart, tokenEnd) });
                    throw ce;
                }
            }
            clusterDestinationChannel(clusterId, destination.getServiceType(), destination.getId(), channelId, endpointUrl, endpointPort, destination.getNetworkSettings().isSharedBackend());
        }
    }
