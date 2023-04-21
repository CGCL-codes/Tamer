    protected void newSetOfCavOnData() {
        String scanPV_string = "";
        String measurePV_string = "";
        if (scanVariable.getChannel() != null) {
            scanPV_string = "xPV=" + scanVariable.getChannel().getId();
        }
        for (int i = 0, n = measuredValuesOnV.size(); i < n; i++) {
            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesOnV.get(i);
            BasicGraphData gd = mv_tmp.getDataContainer();
            if (mv_tmp.getChannel() != null) {
                measurePV_string = mv_tmp.getChannel().getId();
            }
            if (gd != null) {
                gd.setGraphProperty(graphScanOn.getLegendKeyString(), measurePV_string);
            }
        }
        updateGraphOnPanel();
    }
