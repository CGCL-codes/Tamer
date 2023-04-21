    public void showFlowLines() {
        if (flowLines == null) {
            NodeMarkerDataManager nm = getNodeManager();
            DSM2Model model = getModel();
            Channels channels = model.getChannels();
            flowLines = new ArrayList<Polyline>();
            final PolyStyleOptions style = PolyStyleOptions.newInstance("#FF0000", 4, 1.0);
            for (Channel channel : channels.getChannels()) {
                Node upNode = nm.getNodeData(channel.getUpNodeId());
                Node downNode = nm.getNodeData(channel.getDownNodeId());
                LatLng[] points = ModelUtils.getPointsForChannel(channel, upNode, downNode);
                Polyline line = new Polyline(points);
                line.setStrokeStyle(style);
                flowLines.add(line);
            }
        }
        for (Polyline line : flowLines) {
            map.addOverlay(line);
        }
    }
