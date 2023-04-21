    protected void createNewDataContainerRB() {
        if (extGraphDataRB != null) return;
        if (mpv.getChannel() == null) return;
        if (graphDataRBV.size() > 0 && graphDataRBV.lastElement().getNumbOfPoints() == 0) return;
        BasicGraphData gd = new BasicGraphData();
        graphDataRBV.add(gd);
        gd.setImmediateContainerUpdate(false);
        gd.setDrawLinesOn(drawLinesOn);
        if (graphColor != null) {
            gd.setGraphColor(graphColor);
        }
    }
