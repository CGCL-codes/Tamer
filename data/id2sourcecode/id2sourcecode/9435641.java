    private void readScan(XmlDataAdaptor scan2D_Adaptor) {
        ScanVariable scanVariable = theDoc.scanStuff.scanVariable;
        ScanVariable scanVariableParameter = theDoc.scanStuff.scanVariableParameter;
        ScanController2D scanController = theDoc.scanStuff.scanController;
        AvgController avgCntr = theDoc.scanStuff.avgCntr;
        Vector measuredValuesV = theDoc.scanStuff.measuredValuesV;
        FunctionGraphsJPanel graphScan = theDoc.scanStuff.graphScan;
        XmlDataAdaptor params_scan2D = scan2D_Adaptor.childAdaptor(paramsName_SR);
        XmlDataAdaptor paramPV_scan2D = scan2D_Adaptor.childAdaptor(paramPV_SR);
        XmlDataAdaptor scanPV_scan2D = scan2D_Adaptor.childAdaptor(scanPV_SR);
        XmlDataAdaptor measurePVs_scan2D = scan2D_Adaptor.childAdaptor(measurePVs_SR);
        XmlDataAdaptor params_limits = params_scan2D.childAdaptor("limits_step_delay");
        scanController.setLowLimit(params_limits.doubleValue("low"));
        scanController.setUppLimit(params_limits.doubleValue("upp"));
        scanController.setStep(params_limits.doubleValue("step"));
        scanController.setParamLowLimit(params_limits.doubleValue("paramLow"));
        scanController.setParamUppLimit(params_limits.doubleValue("paramUpp"));
        scanController.setParamStep(params_limits.doubleValue("paramStep"));
        scanController.setSleepTime(params_limits.doubleValue("delay"));
        XmlDataAdaptor params_trigger = params_scan2D.childAdaptor("beam_trigger");
        if (params_trigger != null) {
            scanController.setBeamTriggerDelay(params_trigger.doubleValue("delay"));
            scanController.setBeamTriggerState(params_trigger.booleanValue("on"));
        }
        XmlDataAdaptor params_averg = params_scan2D.childAdaptor("averaging");
        avgCntr.setOnOff(params_averg.booleanValue("on"));
        avgCntr.setAvgNumber(params_averg.intValue("N"));
        avgCntr.setTimeDelay(params_averg.doubleValue("delay"));
        XmlDataAdaptor scan_PV_name_DA = scanPV_scan2D.childAdaptor("PV");
        if (scan_PV_name_DA != null) {
            String scan_PV_name = scan_PV_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_name);
            scanVariable.setChannel(channel);
        }
        XmlDataAdaptor scan_PV_RB_name_DA = scanPV_scan2D.childAdaptor("PV_RB");
        if (scan_PV_RB_name_DA != null) {
            String scan_PV_RB_name = scan_PV_RB_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_RB_name);
            scanVariable.setChannelRB(channel);
        }
        java.util.Iterator<XmlDataAdaptor> measuredPVs_children = measurePVs_scan2D.childAdaptorIterator();
        MeasuredValue mv_tmp;
        while (measuredPVs_children.hasNext()) {
            XmlDataAdaptor measuredPV_DA = measuredPVs_children.next();
            String name = measuredPV_DA.stringValue("name");
            boolean onOff = measuredPV_DA.booleanValue("on");
            boolean unWrappedData = false;
            if (measuredPV_DA.stringValue("unWrapped") != null) {
                unWrappedData = measuredPV_DA.booleanValue("unWrapped");
            }
            if (name.equals(FCT1PhaseName)) {
                mv_tmp = theDoc.scanStuff.FCT1PhaseMV;
            } else if (name.equals(FCT2PhaseName)) {
                mv_tmp = theDoc.scanStuff.FCT2PhaseMV;
            } else if (name.equals(FCT3PhaseName)) {
                mv_tmp = theDoc.scanStuff.FCT3PhaseMV;
            } else if (name.equals(cavAmpRBName)) {
                mv_tmp = theDoc.scanStuff.cavAmpRBMV;
            } else if (name.equals(BCMName)) {
                mv_tmp = theDoc.scanStuff.BCMMV;
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
