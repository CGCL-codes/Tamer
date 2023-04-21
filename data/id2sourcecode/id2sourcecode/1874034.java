    private void saveScan(XmlDataAdaptor scan_Adaptor, Dipole dipoleCorr) {
        XmlDataAdaptor params_scan2D = scan_Adaptor.createChild(paramsName_SR);
        XmlDataAdaptor paramPV_scan2D = scan_Adaptor.createChild(paramPV_SR);
        XmlDataAdaptor scanPV_scan2D = scan_Adaptor.createChild(scanPV_SR);
        XmlDataAdaptor measurePVs_scan2D = scan_Adaptor.createChild(measurePVs_SR);
        ScanVariable scanVariableCorr = theDoc.scanStuff.scanVariableCorrX;
        ScanVariable scanVariableQuad = theDoc.scanStuff.scanVariableQuadX;
        ScanController2D scanController = theDoc.scanStuff.scanControllerX;
        AvgController avgCntr = theDoc.scanStuff.avgCntrX;
        Vector measuredValuesV = theDoc.scanStuff.measuredXValuesV;
        FunctionGraphsJPanel graphScan = theDoc.scanStuff.graphScanX;
        ValidationController vldCntr = theDoc.scanStuff.vldCntrX;
        if (dipoleCorr instanceof VDipoleCorr) {
            scanVariableCorr = theDoc.scanStuff.scanVariableCorrY;
            scanVariableQuad = theDoc.scanStuff.scanVariableQuadY;
            scanController = theDoc.scanStuff.scanControllerY;
            avgCntr = theDoc.scanStuff.avgCntrY;
            measuredValuesV = theDoc.scanStuff.measuredYValuesV;
            graphScan = theDoc.scanStuff.graphScanY;
            vldCntr = theDoc.scanStuff.vldCntrY;
        }
        XmlDataAdaptor params_limits = params_scan2D.createChild("limits_step_delay");
        params_limits.setValue("DipoleCorrLow", scanController.getParamLowLimit());
        params_limits.setValue("DipoleCorrUpp", scanController.getParamUppLimit());
        params_limits.setValue("DipoleCorrStep", scanController.getParamStep());
        params_limits.setValue("QuadLow", scanController.getLowLimit());
        params_limits.setValue("QuadUpp", scanController.getUppLimit());
        params_limits.setValue("QuadStep", scanController.getStep());
        params_limits.setValue("delay", scanController.getSleepTime());
        XmlDataAdaptor params_trigger = params_scan2D.createChild("beam_trigger");
        params_trigger.setValue("on", scanController.getBeamTriggerState());
        params_trigger.setValue("delay", scanController.getBeamTriggerDelay());
        XmlDataAdaptor params_averg = params_scan2D.createChild("averaging");
        params_averg.setValue("on", avgCntr.isOn());
        params_averg.setValue("N", avgCntr.getAvgNumber());
        params_averg.setValue("delay", avgCntr.getTimeDelay());
        XmlDataAdaptor params_vld = params_scan2D.createChild("validation");
        params_vld.setValue("on", vldCntr.isOn());
        params_vld.setValue("low", vldCntr.getLowLim());
        params_vld.setValue("upp", vldCntr.getUppLim());
        if (scanVariableCorr.getChannel() != null) {
            XmlDataAdaptor scan_PV_name = paramPV_scan2D.createChild("CorrPV");
            scan_PV_name.setValue("name", scanVariableCorr.getChannelName());
        }
        if (scanVariableCorr.getChannelRB() != null) {
            XmlDataAdaptor scan_PV_RB_name = paramPV_scan2D.createChild("CorrPV_RB");
            scan_PV_RB_name.setValue("name", scanVariableCorr.getChannelNameRB());
        }
        if (scanVariableQuad.getChannel() != null) {
            XmlDataAdaptor scan_PV_name = scanPV_scan2D.createChild("QuadPV");
            scan_PV_name.setValue("name", scanVariableQuad.getChannelName());
        }
        if (scanVariableQuad.getChannelRB() != null) {
            XmlDataAdaptor scan_PV_RB_name = scanPV_scan2D.createChild("QuadPV_RB");
            scan_PV_RB_name.setValue("name", scanVariableQuad.getChannelNameRB());
        }
        if (dipoleCorr instanceof HDipoleCorr) {
            writeMeasuredValue(theDoc.scanStuff.BPM1XPosMV, measurePVs_scan2D, centerBPMXName, dipoleCorr);
            for (int i = 0; i < theDoc.BPM2.length; i++) writeMeasuredValue(theDoc.scanStuff.BPM2XPosMV[i], measurePVs_scan2D, (downstreamBPMXName + i), dipoleCorr);
            writeMeasuredValue(theDoc.scanStuff.BPM3XPosMV, measurePVs_scan2D, upstreamBPMXName, dipoleCorr);
            writeMeasuredValue(theDoc.scanStuff.BCMMVX, measurePVs_scan2D, BCMXName, dipoleCorr);
        }
        if (dipoleCorr instanceof VDipoleCorr) {
            writeMeasuredValue(theDoc.scanStuff.BPM1YPosMV, measurePVs_scan2D, centerBPMYName, dipoleCorr);
            for (int i = 0; i < theDoc.BPM2.length; i++) writeMeasuredValue(theDoc.scanStuff.BPM2YPosMV[i], measurePVs_scan2D, (downstreamBPMYName + i), dipoleCorr);
            writeMeasuredValue(theDoc.scanStuff.BPM3YPosMV, measurePVs_scan2D, upstreamBPMYName, dipoleCorr);
            writeMeasuredValue(theDoc.scanStuff.BCMMVY, measurePVs_scan2D, BCMYName, dipoleCorr);
        }
    }
