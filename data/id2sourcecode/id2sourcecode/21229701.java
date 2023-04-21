    public void updateXSLine() {
        if (currentlySelectedLine != null) {
            if (currentlySelectedXSection != null) {
                Channel channel = ModelUtils.getChannelForXSection(currentlySelectedXSection, mapPanel.getChannelManager().getChannels());
                Node upNode = mapPanel.getNodeManager().getNodes().getNode(channel.getUpNodeId());
                Node downNode = mapPanel.getNodeManager().getNodes().getNode(channel.getDownNodeId());
                mapPanel.getChannelManager().removeAndAddPolylineForXSection(currentlySelectedXSection, channel, upNode, downNode);
            }
        }
    }
