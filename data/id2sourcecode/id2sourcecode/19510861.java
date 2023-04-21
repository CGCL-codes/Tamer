    public void onClick(MapClickEvent event) {
        int type = controlPanel.getAddTypeSelected();
        if ((type != ElementType.CHANNEL) && (type != ElementType.XSECTION)) {
            LatLng latLng = event.getLatLng();
            if (latLng == null) {
                String msg = "You clicked on a marker. If you'd like to add a node, click on the map instead. You can move it overlap later.";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (type == ElementType.NODE) {
                NodeMarkerDataManager nodeManager = mapPanel.getNodeManager();
                Node n = new Node();
                n.setId(nodeManager.getNewNodeId());
                n.setLatitude(latLng.getLatitude());
                n.setLongitude(latLng.getLongitude());
                mapPanel.getNodeManager().addNode(n);
            } else if (type == ElementType.GATE) {
                GateOverlayManager gateManager = mapPanel.getGateManager();
                Gate g = new Gate();
                g.setName("GATE_" + (gateManager.getNumberOfGates() + 1));
                g.setLatitude(latLng.getLatitude());
                g.setLongitude(latLng.getLongitude());
                gateManager.addGate(g);
            } else if (type == ElementType.RESERVOIR) {
                ReservoirOverlayManager reservoirManager = mapPanel.getReservoirManager();
                Reservoir r = new Reservoir();
                r.setLatitude(latLng.getLatitude());
                r.setLongitude(latLng.getLongitude());
                r.setName("RESERVOIR_" + (reservoirManager.getNumberOfReservoirs() + 1));
                reservoirManager.addReservoir(r);
            } else if (type == ElementType.OUTPUT) {
            }
        } else if (type == ElementType.CHANNEL) {
            Overlay overlay = event.getOverlay();
            if (overlay == null) {
                String msg = "You have selected adding a channel and did not click on a node marker";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            NodeMarkerDataManager nodeManager = mapPanel.getNodeManager();
            Node node = nodeManager.getNodeForMarker(overlay);
            if (node == null) {
                String msg = "You have selected adding a channel and clicked on a marker that is not a node!";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (previousNode == null) {
                String msg = "Adding a channel with upnode (" + node.getId() + "). Now click on downnode.";
                eventBus.fireEvent(new MessageEvent(msg));
                if (channelLineHandler == null) {
                    channelLineHandler = new ChannelLineMouseMoveHandler();
                }
                channelLineHandler.startLine();
                previousNode = node;
            } else {
                String msg = "Adding channel with upnode (" + previousNode.getId() + ") and downnode (" + node.getId() + ")";
                eventBus.fireEvent(new MessageEvent(msg));
                ChannelLineDataManager channelManager = mapPanel.getChannelManager();
                Channel channel = new Channel();
                channel.setUpNodeId(previousNode.getId());
                channel.setDownNodeId(node.getId());
                channel.setId(channelManager.getNewChannelId());
                channelManager.addChannel(channel);
                channelLineHandler.clearLine();
                previousNode = null;
            }
        } else if (type == ElementType.XSECTION) {
            Overlay overlay = event.getOverlay();
            if (overlay == null) {
                String msg = "You have selected adding a xsection and did not click on a channel connection line";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (!(overlay instanceof Polyline)) {
                String msg = "When adding a xsection click on a channel connection line. You clicked on a marker?";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            String channelId = mapPanel.getChannelManager().getChannelId(overlay);
            if (channelId == null) {
                String msg = "When adding a xsection click on a channel connection line. You clicked on some other line?";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            XSection xsection = ModelUtils.createXSection(channelId, 0.5, 1000.0);
            mapPanel.getChannelManager().getChannels().getChannel(channelId).addXSection(xsection);
        }
    }
