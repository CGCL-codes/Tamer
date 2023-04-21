    private void readScan(XmlDataAdaptor scan2D_Adaptor, ScanVariable scanVariableQuad, ScanVariable scanVariableCorr, ScanController2D scanController, AvgController avgCntr, Vector measuredValuesV, FunctionGraphsJPanel graphScan, ValidationController vldCntr) {
        XmlDataAdaptor params_scan2D = scan2D_Adaptor.childAdaptor(paramsName_SR);
        XmlDataAdaptor paramPV_scan2D = scan2D_Adaptor.childAdaptor(paramPV_SR);
        XmlDataAdaptor scanPV_scan2D = scan2D_Adaptor.childAdaptor(scanPV_SR);
        XmlDataAdaptor measurePVs_scan2D = scan2D_Adaptor.childAdaptor(measurePVs_SR);
        XmlDataAdaptor params_limits = params_scan2D.childAdaptor("limits_step_delay");
        scanController.setLowLimit(params_limits.doubleValue("QuadLow"));
        scanController.setUppLimit(params_limits.doubleValue("QuadUpp"));
        scanController.setStep(params_limits.doubleValue("QuadStep"));
        scanController.setParamLowLimit(params_limits.doubleValue("DipoleCorrLow"));
        scanController.setParamUppLimit(params_limits.doubleValue("DipoleCorrUpp"));
        scanController.setParamStep(params_limits.doubleValue("DipoleCorrStep"));
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
        XmlDataAdaptor params_vld = params_scan2D.childAdaptor("validation");
        vldCntr.setOnOff(params_vld.booleanValue("on"));
        vldCntr.setUppLim(params_vld.doubleValue("upp"));
        vldCntr.setLowLim(params_vld.doubleValue("low"));
        XmlDataAdaptor param_PV_name_DA = paramPV_scan2D.childAdaptor("PV");
        if (param_PV_name_DA != null) {
            String param_PV_name = param_PV_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(param_PV_name);
            scanVariableCorr.setChannel(channel);
        }
        XmlDataAdaptor param_PV_RB_name_DA = scanPV_scan2D.childAdaptor("PV_RB");
        if (param_PV_RB_name_DA != null) {
            String scan_PV_RB_name = param_PV_RB_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_RB_name);
            scanVariableCorr.setChannelRB(channel);
        }
        XmlDataAdaptor scan_PV_name_DA = scanPV_scan2D.childAdaptor("PV");
        if (scan_PV_name_DA != null) {
            String scan_PV_name = scan_PV_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_name);
            scanVariableQuad.setChannel(channel);
        }
        XmlDataAdaptor scan_PV_RB_name_DA = scanPV_scan2D.childAdaptor("PV_RB");
        if (scan_PV_RB_name_DA != null) {
            String scan_PV_RB_name = scan_PV_RB_name_DA.stringValue("name");
            Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_RB_name);
            scanVariableQuad.setChannelRB(channel);
        }
        java.util.Iterator<XmlDataAdaptor> measuredPVs_children = measurePVs_scan2D.childAdaptorIterator();
        MeasuredValue mv_tmp = null;
        while (measuredPVs_children.hasNext()) {
            XmlDataAdaptor measuredPV_DA = measuredPVs_children.next();
            String name = measuredPV_DA.stringValue("name");
            boolean onOff = measuredPV_DA.booleanValue("on");
            if (name.equals(centerBPMXName)) {
                mv_tmp = theDoc.scanStuff.BPM1XPosMV;
            } else if (name.equals(centerBPMYName)) {
                mv_tmp = theDoc.scanStuff.BPM1YPosMV;
            } else if (name.startsWith("downstreamBPM")) {
                for (int i = 0; i < downstreamBPMNum; i++) {
                    if (name.equals(("downstreamBPMX" + i))) {
                        mv_tmp = theDoc.scanStuff.BPM2XPosMV[i];
                    } else if (name.equals(("downstreamBPMY" + i))) {
                        mv_tmp = theDoc.scanStuff.BPM2YPosMV[i];
                    }
                }
            } else if (name.equals(upstreamBPMXName)) {
                mv_tmp = theDoc.scanStuff.BPM3XPosMV;
            } else if (name.equals(upstreamBPMYName)) {
                mv_tmp = theDoc.scanStuff.BPM3YPosMV;
            } else if (name.equals(quadMagXName)) {
                mv_tmp = theDoc.scanStuff.QuadMagXSetMV;
            } else if (name.equals(quadMagYName)) {
                mv_tmp = theDoc.scanStuff.QuadMagYSetMV;
            } else if (name.equals(BCMXName)) {
                mv_tmp = theDoc.scanStuff.BCMMVX;
            } else if (name.equals(BCMYName)) {
                mv_tmp = theDoc.scanStuff.BCMMVY;
            } else {
                String errText = "Oh no!, an unidentified set of measured data was encountered while reading the setup file";
                theDoc.myWindow().errorText.setText(errText);
                System.err.println(errText);
                return;
            }
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
