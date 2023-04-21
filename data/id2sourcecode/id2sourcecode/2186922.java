    private LatLng calculateMarkerLocation(String channelId, double distance) {
        Channel channel = model.getChannels().getChannel(channelId);
        if (channel == null) {
            return null;
        }
        double distanceNormalized = distance / channel.getLength();
        String upNodeId = channel.getUpNodeId();
        String downNodeId = channel.getDownNodeId();
        Node upNode = model.getNodes().getNode(upNodeId);
        Node downNode = model.getNodes().getNode(downNodeId);
        double x2 = downNode.getLatitude();
        double y2 = downNode.getLongitude();
        double x1 = upNode.getLatitude();
        double y1 = upNode.getLongitude();
        double slope = (y2 - y1) / (x2 - x1);
        double xn = x1 + distanceNormalized * (x2 - x1);
        double yn = slope * (xn - x1) + y1;
        return LatLng.newInstance(xn, yn);
    }
