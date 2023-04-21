    protected void newSetOfCavOffData() {
        String scanPV_string = "";
        String measurePV_string = "";
        if (scanVariable.getChannel() != null) {
            scanPV_string = "xPV=" + scanVariable.getChannel().getId();
        }
        for (int i = 0, n = measuredValuesOffV.size(); i < n; i++) {
            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesOffV.get(i);
            BasicGraphData gd = mv_tmp.getDataContainer();
            if (mv_tmp.getChannel() != null) {
                measurePV_string = mv_tmp.getChannel().getId();
            }
            if (gd != null) {
            }
        }
        updateGraphOffPanel();
    }
