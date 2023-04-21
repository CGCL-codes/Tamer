    private void newSetOfData1D() {
        DecimalFormat valueFormat = new DecimalFormat("###.###");
        String scanPV_string = "";
        String measurePV_string = "";
        String legend_string = "";
        if (scanVariable.getChannel() != null) {
            scanPV_string = "xPV=" + scanVariable.getChannel().getId();
        }
        for (int i = 0, n = measuredValuesOffV.size(); i < n; i++) {
            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesOffV.get(i);
            BasicGraphData gd = mv_tmp.getDataContainer();
            if (mv_tmp.getChannel() != null) {
                measurePV_string = mv_tmp.getChannel().getId();
            }
            legend_string = measurePV_string + " ";
            if (gd != null) {
                gd.removeAllPoints();
                gd.setGraphProperty(graphScan.getLegendKeyString(), legend_string);
            }
            if (scanVariable.getChannelRB() != null) {
                gd = mv_tmp.getDataContainerRB();
            }
        }
        updateGraph1DPanel();
        theDoc.setHasChanges(true);
    }
