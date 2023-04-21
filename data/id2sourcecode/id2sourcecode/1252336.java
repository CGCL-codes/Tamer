    private void newSetOfData(ScanController2D scanController, ScanVariable scanVariableCorr, ScanVariable scanVariableQuad, Vector measuredValuesGraphV, FunctionGraphsJPanel graphScan) {
        DecimalFormat valueFormat = new DecimalFormat("###.###");
        String paramPV_string = "";
        String scanPV_string = "";
        String measurePV_string = "";
        String legend_string = "";
        Double paramValue = new Double(scanController.getParamValue());
        Double paramValueRB = new Double(scanController.getParamValueRB());
        if (scanVariableCorr.getChannel() != null) {
            String paramValString = valueFormat.format(scanVariableCorr.getValue());
            paramPV_string = paramPV_string + " par.PV : " + scanVariableCorr.getChannel().getId() + "=" + paramValString;
            paramValue = new Double(scanVariableCorr.getValue());
        } else {
            paramPV_string = paramPV_string + " param.= " + paramValue;
        }
        if (scanVariableQuad.getChannel() != null) {
            scanPV_string = "xPV=" + scanVariableQuad.getChannel().getId();
        }
        for (int i = 0, n = measuredValuesGraphV.size(); i < n; i++) {
            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesGraphV.get(i);
            BasicGraphData gd = mv_tmp.getDataContainer();
            if (mv_tmp.getChannel() != null) {
                measurePV_string = mv_tmp.getChannel().getId();
            }
            legend_string = measurePV_string + paramPV_string + " ";
            if (gd != null) {
                gd.removeAllPoints();
                gd.setGraphProperty(graphScan.getLegendKeyString(), legend_string);
                if (paramValue != null) gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                if (paramValueRB != null) gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
            }
            if (scanVariableQuad.getChannelRB() != null) {
                gd = mv_tmp.getDataContainerRB();
                if (gd != null) {
                    if (paramValue != null) gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                    if (paramValueRB != null) gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
                }
            }
        }
        updateGraphPanel(measuredValuesGraphV, graphScan);
        theDoc.setHasChanges(true);
    }
