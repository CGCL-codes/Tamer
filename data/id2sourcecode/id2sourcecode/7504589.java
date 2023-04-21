    public void populateGrid() {
        clearAllMarkers();
        if (model == null) {
            return;
        }
        setNodeManager(new NodeMarkerDataManager(this, model.getNodes()));
        setChannelManager(new ChannelLineDataManager(this, model.getChannels()));
        refreshGrid();
    }
