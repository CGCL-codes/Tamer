    public Polyline addPolylineForChannel(Channel channel) {
        Node upNode = mapPanel.getNodeManager().getNodeData(channel.getUpNodeId());
        Node downNode = mapPanel.getNodeManager().getNodeData(channel.getDownNodeId());
        if ((upNode == null) || (downNode == null)) {
            return null;
        }
        LatLng upPoint = LatLng.newInstance(upNode.getLatitude(), upNode.getLongitude());
        LatLng downPoint = LatLng.newInstance(downNode.getLatitude(), downNode.getLongitude());
        LatLng[] points = null;
        ArrayList<String> channelsWithNodes = ModelUtils.getChannelsWithNodes(upNode, downNode, getChannels());
        if (channelsWithNodes.size() > 1) {
            Collections.sort(channelsWithNodes);
            int n = channelsWithNodes.size();
            int i = 0;
            for (i = 0; i < channelsWithNodes.size(); i++) {
                if (channelsWithNodes.get(i).equals(channel.getId())) {
                    break;
                }
            }
            double lat = upPoint.getLatitude() + downPoint.getLatitude();
            double lon = upPoint.getLongitude() + downPoint.getLongitude();
            LatLng midPoint = LatLng.newInstance(lat / 2 + (i - n / 2.0) * 0.001, lon / 2 + (i - n / 2.0) * 0.001);
            points = new LatLng[] { upPoint, midPoint, downPoint };
        } else {
            points = new LatLng[] { upPoint, downPoint };
        }
        Polyline line = null;
        if (!ENCODE_POLYLINES) {
            line = new Polyline(points);
            line.setStrokeStyle(getPolylineStyle());
        } else {
            line = encoder.dpEncodeToGPolyline(points, getLineColor(), weight, opacity);
        }
        addPolyline(channel.getId(), line);
        line.addPolylineMouseOverHandler(new PolylineMouseOverHandler() {

            public void onMouseOver(PolylineMouseOverEvent event) {
                WindowUtils.changeCursor("pointer");
            }
        });
        line.addPolylineClickHandler(channelClickHandler);
        mapPanel.getMap().addOverlay(line);
        return line;
    }
