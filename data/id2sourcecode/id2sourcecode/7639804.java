    public void removeNode(String nodeId, Channels channels) {
        Node node = mapPanel.getNodeManager().getNodes().getNode(nodeId);
        if (node == null) {
            return;
        }
        String channelsConnectedTo = ModelUtils.getChannelsConnectedTo(channels, node);
        if (channelsConnectedTo != null) {
            throw new RuntimeException("Cannot delete node connected to channels: " + channelsConnectedTo);
        }
        removeMarkerForNode(node);
        nodes.removeNode(node);
    }
