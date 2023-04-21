    private void saveScan1D(XmlDataAdaptor scan1D_Adaptor) {
        XmlDataAdaptor scanPV_scan1D = scan1D_Adaptor.createChild(scanPV_SR);
        XmlDataAdaptor measurePVs_scan1D = scan1D_Adaptor.createChild(measureOffPVs_SR);
        ScanVariable scanVariable = theDoc.scanStuff.scanVariable;
        ScanVariable scanVariableParameter = theDoc.scanStuff.scanVariableParameter;
        ScanController1D scanController = theDoc.scanStuff.scanController1D;
        AvgController avgCntr = theDoc.scanStuff.avgCntr1D;
        Vector measuredValuesV = theDoc.scanStuff.measuredValuesOffV;
        FunctionGraphsJPanel graphScan = theDoc.scanStuff.graphScan1D;
        XmlDataAdaptor scan_params_DA = scan1D_Adaptor.createChild("scan_params");
        XmlDataAdaptor scan_limits_DA = scan_params_DA.createChild("limits_step_delay");
        scan_limits_DA.setValue("low", scanController.getLowLimit());
        scan_limits_DA.setValue("upp", scanController.getUppLimit());
        scan_limits_DA.setValue("step", scanController.getStep());
        scan_limits_DA.setValue("delay", scanController.getSleepTime());
        XmlDataAdaptor params_trigger = scan_params_DA.createChild("beam_trigger");
        params_trigger.setValue("on", scanController.getBeamTriggerState());
        params_trigger.setValue("delay", scanController.getBeamTriggerDelay());
        XmlDataAdaptor params_averg = scan_params_DA.createChild("averaging");
        params_averg.setValue("on", avgCntr.isOn());
        params_averg.setValue("N", avgCntr.getAvgNumber());
        params_averg.setValue("delay", avgCntr.getTimeDelay());
        if (scanVariable.getChannel() != null) {
            XmlDataAdaptor scan_PV_name = scanPV_scan1D.createChild("PV");
            scan_PV_name.setValue("name", scanVariable.getChannelName());
        }
        if (scanVariable.getChannelRB() != null) {
            XmlDataAdaptor scan_PV_RB_name = scanPV_scan1D.createChild("PV_RB");
            scan_PV_RB_name.setValue("name", scanVariable.getChannelNameRB());
        }
        writeMeasuredValue(theDoc.scanStuff.BPM1PhaseOffMV, measurePVs_scan1D, BPM1PhaseName);
        writeMeasuredValue(theDoc.scanStuff.BPM2PhaseOffMV, measurePVs_scan1D, BPM2PhaseName);
        writeMeasuredValue(theDoc.scanStuff.BCMOffMV, measurePVs_scan1D, BCMName);
    }
