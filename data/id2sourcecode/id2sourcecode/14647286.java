    public static LatLng[] calculateEndPoints(XSection xSection, Channel channel, Node upNode, Node downNode) {
        LatLng[] channelOutlinePoints = getChannelOutlinePoints(channel, upNode, downNode);
        double distance = xSection.getDistance();
        distance = channel.getLength() * distance;
        int segmentIndex = GeomUtils.findSegmentAtDistance(channelOutlinePoints, distance);
        LatLng point1 = channelOutlinePoints[segmentIndex];
        LatLng point2 = channelOutlinePoints[segmentIndex + 1];
        double segmentDistance = GeomUtils.findDistanceUptoSegment(segmentIndex, channelOutlinePoints);
        LatLng point0 = GeomUtils.findPointAtDistance(point1, point2, distance - segmentDistance);
        double slope = GeomUtils.getSlopeBetweenPoints(point1, point2);
        double width = getMaxTopWidth(xSection);
        return GeomUtils.getLineWithSlopeOfLengthAndCenteredOnPoint(-1 / slope, width, point0);
    }
