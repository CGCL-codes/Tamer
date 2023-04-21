    public void doOnClick(PolylineClickEvent event) {
        Polyline channelLine = event.getSender();
        String channelId = mapPanel.getChannelManager().getChannelId(channelLine);
        if (channelId == null) {
            return;
        }
        final Channel channel = mapPanel.getChannelManager().getChannels().getChannel(channelId);
        if (channel == null) {
            mapPanel.showMessage("No channel found for " + channelId);
            return;
        }
        infoPanel = new ChannelInfoPanel(channel, mapPanel);
        mapPanel.getInfoPanel().clear();
        if (mapPanel.isInEditMode() && mapPanel.isInDeletingMode()) {
            mapPanel.getChannelManager().removeChannel(channel);
            if (line != null) {
                mapPanel.getMap().removeOverlay(line);
                line = null;
            }
            return;
        }
        mapPanel.getInfoPanel().add(infoPanel);
        NodeMarkerDataManager nodeManager = mapPanel.getNodeManager();
        Node upNode = nodeManager.getNodeData(channel.getUpNodeId());
        Node downNode = nodeManager.getNodeData(channel.getDownNodeId());
        if (line != null) {
            clearOverlays();
        }
        PolyStyleOptions style = PolyStyleOptions.newInstance(color, weight, opacity);
        LatLng[] points = ModelUtils.getPointsForChannel(channel, upNode, downNode);
        line = new Polyline(points);
        mapPanel.getMap().addOverlay(line);
        line.setStrokeStyle(style);
        if (mapPanel.isInEditMode()) {
            line.setEditingEnabled(PolyEditingOptions.newInstance(MAX_VERTEX_FLOWLINE));
            line.addPolylineClickHandler(new PolylineClickHandler() {

                public void onClick(PolylineClickEvent event) {
                    updateChannelLengthLatLng(channel);
                    line.setEditingEnabled(false);
                    clearOverlays();
                    updateDisplay(channel);
                }
            });
            line.addPolylineLineUpdatedHandler(new PolylineLineUpdatedHandler() {

                public void onUpdate(PolylineLineUpdatedEvent event) {
                    updateChannelLengthLatLng(channel);
                    updateDisplay(channel);
                }
            });
            drawXSectionLines(channel);
        } else {
            line.addPolylineClickHandler(new PolylineClickHandler() {

                public void onClick(PolylineClickEvent event) {
                    clearOverlays();
                    updateDisplay(channel);
                }
            });
            line.addPolylineMouseOverHandler(new PolylineMouseOverHandler() {

                public void onMouseOver(PolylineMouseOverEvent event) {
                    WindowUtils.changeCursor("pointer");
                }
            });
            drawXSectionLines(channel);
        }
    }
