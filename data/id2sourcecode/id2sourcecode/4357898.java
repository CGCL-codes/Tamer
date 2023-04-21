    protected void createNewDataContainer() {
        if (extGraphData != null) return;
        if (mpv.getChannel() == null) return;
        if (graphDataV.size() > 0 && ((BasicGraphData) graphDataV.lastElement()).getNumbOfPoints() == 0) return;
        BasicGraphData gd = new BasicGraphData();
        graphDataV.add(gd);
        gd.setImmediateContainerUpdate(false);
        gd.setDrawLinesOn(drawLinesOn);
        if (graphColor != null) {
            gd.setGraphColor(graphColor);
        }
    }
