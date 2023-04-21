    private void newSetOfData() {
        DecimalFormat valueFormat = new DecimalFormat("###.###");
        String paramPV_string = "";
        String measurePV_string = "";
        String legend_string = "";
        Double paramValue = new Double(scanController.getParamValue());
        Double paramValueRB = new Double(scanController.getParamValueRB());
        if (scanVariableParameter.getChannel() != null) {
            String paramValString = valueFormat.format(scanVariableParameter.getValue());
            paramPV_string = paramPV_string + " par.PV : " + scanVariableParameter.getChannel().getId() + "=" + paramValString;
            paramValue = new Double(scanVariableParameter.getValue());
        } else {
            paramPV_string = paramPV_string + " param.= " + paramValue;
        }
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            MeasuredValue mv_tmp = measuredValuesV.get(i);
            BasicGraphData gd = mv_tmp.getDataContainer();
            if (mv_tmp.getChannel() != null) {
                measurePV_string = mv_tmp.getChannel().getId();
            }
            legend_string = mv_tmp.getAlias() + ": " + measurePV_string + paramPV_string + " ";
            if (gd != null) {
                gd.removeAllPoints();
                gd.setGraphProperty(graphScan.getLegendKeyString(), legend_string);
                if (paramValue != null) gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                if (paramValueRB != null) gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
            }
            if (scanVariable.getChannelRB() != null) {
                gd = mv_tmp.getDataContainerRB();
                if (gd != null) {
                    if (paramValue != null) gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                    if (paramValueRB != null) gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
                }
            }
        }
        updateGraphPanel();
        theDoc.setHasChanges(true);
    }
