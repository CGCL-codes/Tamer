    private void readScan1D(XmlDataAdaptor scan1D_Adaptor) {
        ScanVariable scanVariable = theDoc.scanStuff.scanVariable;
        ScanController1D scanController = theDoc.scanStuff.scanController1D;
        AvgController avgCntr = theDoc.scanStuff.avgCntr1D;
        Vector measuredValuesV = theDoc.scanStuff.measuredValuesOffV;
        FunctionGraphsJPanel graphScan = theDoc.scanStuff.graphScan1D;
        XmlDataAdaptor scanPV_scan1D = scan1D_Adaptor.childAdaptor(scanPV_SR);
        XmlDataAdaptor measurePVs_scan1D = scan1D_Adaptor.childAdaptor(measureOffPVs_SR);
        XmlDataAdaptor scan_params_DA = scan1D_Adaptor.childAdaptor("scan_params");
        if (scan_params_DA != null) {
            XmlDataAdaptor scan_limits_DA = scan_params_DA.childAdaptor("limits_step_delay");
            if (scan_limits_DA != null) {
                scanController.setLowLimit(scan_limits_DA.doubleValue("low"));
                scanController.setUppLimit(scan_limits_DA.doubleValue("upp"));
                scanController.setStep(scan_limits_DA.doubleValue("step"));
                scanController.setSleepTime(scan_limits_DA.doubleValue("delay"));
            }
            XmlDataAdaptor params_trigger = scan_params_DA.childAdaptor("beam_trigger");
            if (params_trigger != null) {
                scanController.setBeamTriggerDelay(params_trigger.doubleValue("delay"));
                scanController.setBeamTriggerState(params_trigger.booleanValue("on"));
            }
            XmlDataAdaptor params_averg = scan_params_DA.childAdaptor("averaging");
            avgCntr.setOnOff(params_averg.booleanValue("on"));
            avgCntr.setAvgNumber(params_averg.intValue("N"));
            avgCntr.setTimeDelay(params_averg.doubleValue("delay"));
        }
        XmlDataAdaptor scan_PV_DA = scan1D_Adaptor.childAdaptor("scan_PV");
        if (scan_PV_DA != null) {
            XmlDataAdaptor scan_PV_name_DA = scan_PV_DA.childAdaptor("PV");
            if (scan_PV_name_DA != null) {
                String scan_PV_name = scan_PV_name_DA.stringValue("name");
                if (scan_PV_name != null) {
                    Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_name);
                    scanVariable.setChannel(channel);
                }
            }
            XmlDataAdaptor scan_PV_RB_name_DA = scan_PV_DA.childAdaptor("PV_RB");
            if (scan_PV_RB_name_DA != null) {
                String scan_PV_RB_name = scan_PV_RB_name_DA.stringValue("name");
                Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_RB_name);
                scanVariable.setChannelRB(channel);
            }
        }
        java.util.Iterator<XmlDataAdaptor> measuredPVs_children = measurePVs_scan1D.childAdaptorIterator();
        MeasuredValue mv_tmp;
        while (measuredPVs_children.hasNext()) {
            XmlDataAdaptor measuredPV_DA = measuredPVs_children.next();
            String name = measuredPV_DA.stringValue("name");
            boolean onOff = measuredPV_DA.booleanValue("on");
            boolean unWrappedData = false;
            if (measuredPV_DA.stringValue("unWrapped") != null) {
                unWrappedData = measuredPV_DA.booleanValue("unWrapped");
            }
            if (name.equals(BPM1PhaseName)) {
                mv_tmp = theDoc.scanStuff.BPM1PhaseOffMV;
            } else if (name.equals(BPM2PhaseName)) {
                mv_tmp = theDoc.scanStuff.BPM2PhaseOffMV;
            } else if (name.equals(BCMName)) {
                mv_tmp = theDoc.scanStuff.BCMOffMV;
            } else {
                String errText = "Oh no!, an unidentified set of measured data was encountered while reading the setup file";
                theDoc.myWindow().errorText.setText(errText);
                System.err.println(errText);
                return;
            }
            mv_tmp.generateUnwrappedData(unWrappedData);
            java.util.Iterator<XmlDataAdaptor> dataIt = measuredPV_DA.childAdaptorIterator("Graph_For_scanPV");
            while (dataIt.hasNext()) {
                BasicGraphData gd = new BasicGraphData();
                mv_tmp.addNewDataConatainer(gd);
                XmlDataAdaptor data = dataIt.next();
                String legend = data.stringValue("legend");
                XmlDataAdaptor paramDataValue = data.childAdaptor("parameter_value");
                if (paramDataValue != null) {
                    double parameter_value = paramDataValue.doubleValue("value");
                    gd.setGraphProperty("PARAMETER_VALUE", new Double(parameter_value));
                }
                XmlDataAdaptor paramDataValueRB = data.childAdaptor("parameter_value_RB");
                if (paramDataValueRB != null) {
                    double parameter_value_RB = paramDataValueRB.doubleValue("value");
                    gd.setGraphProperty("PARAMETER_VALUE_RB", new Double(parameter_value_RB));
                }
                gd.setGraphProperty(graphScan.getLegendKeyString(), legend);
                java.util.Iterator<XmlDataAdaptor> xyerrIt = data.childAdaptorIterator("XYErr");
                while (xyerrIt.hasNext()) {
                    XmlDataAdaptor xyerr = xyerrIt.next();
                    gd.addPoint(xyerr.doubleValue("x"), xyerr.doubleValue("y"), xyerr.doubleValue("err"));
                }
            }
            dataIt = measuredPV_DA.childAdaptorIterator("Graph_For_scanPV_RB");
            while (dataIt.hasNext()) {
                XmlDataAdaptor data = dataIt.next();
                String legend = data.stringValue("legend");
                BasicGraphData gd = new BasicGraphData();
                mv_tmp.addNewDataConatainerRB(gd);
                if (gd != null) {
                    gd.setGraphProperty(graphScan.getLegendKeyString(), legend);
                    java.util.Iterator<XmlDataAdaptor> xyerrIt = data.childAdaptorIterator("XYErr");
                    while (xyerrIt.hasNext()) {
                        XmlDataAdaptor xyerr = xyerrIt.next();
                        gd.addPoint(xyerr.doubleValue("x"), xyerr.doubleValue("y"), xyerr.doubleValue("err"));
                    }
                }
            }
        }
    }
