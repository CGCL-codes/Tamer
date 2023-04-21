    public AreaData getTopologyAreaData() {
        if (topologyAreaData == null) {
            topologyAreaData = new AreaData(zone.getTopology());
            topologyAreaData.digest();
        }
        return topologyAreaData;
    }
