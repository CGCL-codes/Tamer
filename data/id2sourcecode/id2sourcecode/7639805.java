    public void renameNodeId(String newValue, String previousValue) {
        nodes.renameNodeId(newValue, previousValue);
        mapPanel.getMap().removeOverlay(getMarkerFor(previousValue));
        addMarkerForNode(nodes.getNode(newValue));
        mapPanel.getChannelManager().getChannels().updateNodeId(newValue, previousValue);
        mapPanel.getReservoirManager().getReservoirs().updateNodeId(newValue, previousValue);
        mapPanel.getGateManager().getGates().updateNodeId(newValue, previousValue);
        mapPanel.getBoundaryManager().getBoundaryInputs().updateNodeId(newValue, previousValue);
    }
