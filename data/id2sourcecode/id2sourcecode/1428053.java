    public void onDragEnd(MarkerDragEndEvent event) {
        Marker marker = event.getSender();
        LatLng latLng = marker.getLatLng();
        String id = marker.getTitle();
        Node nodeData = mapPanel.getNodeManager().getNodeData(id);
        nodeData.setLatitude(latLng.getLatitude());
        nodeData.setLongitude(latLng.getLongitude());
        if (mapPanel.getChannelManager() == null) {
            return;
        }
        List<String> channelIds = mapPanel.getChannelManager().getChannelsForNodeId(nodeData.getId());
        for (String channelId : channelIds) {
            mapPanel.getChannelManager().removePolyline(channelId);
            mapPanel.getChannelManager().addPolylineForChannel(mapPanel.getChannelManager().getChannels().getChannel(channelId));
        }
    }
