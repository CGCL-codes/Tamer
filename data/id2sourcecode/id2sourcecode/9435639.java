    private void saveScan2D(XmlDataAdaptor scan2D_Adaptor) {
        XmlDataAdaptor params_scan2D = scan2D_Adaptor.createChild(paramsName_SR);
        XmlDataAdaptor paramPV_scan2D = scan2D_Adaptor.createChild(paramPV_SR);
        XmlDataAdaptor scanPV_scan2D = scan2D_Adaptor.createChild(scanPV_SR);
        XmlDataAdaptor measurePVs_scan2D = scan2D_Adaptor.createChild(measurePVs_SR);
        ScanVariable scanVariable = theDoc.scanStuff.scanVariable;
        ScanVariable scanVariableParameter = theDoc.scanStuff.scanVariableParameter;
        ScanController2D scanController = theDoc.scanStuff.scanController;
        AvgController avgCntr = theDoc.scanStuff.avgCntr;
        Vector measuredValuesV = theDoc.scanStuff.measuredValuesV;
        FunctionGraphsJPanel graphScan = theDoc.scanStuff.graphScan;
        XmlDataAdaptor params_limits = params_scan2D.createChild("limits_step_delay");
        params_limits.setValue("paramLow", scanController.getParamLowLimit());
        params_limits.setValue("paramUpp", scanController.getParamUppLimit());
        params_limits.setValue("paramStep", scanController.getParamStep());
        params_limits.setValue("low", scanController.getLowLimit());
        params_limits.setValue("upp", scanController.getUppLimit());
        params_limits.setValue("step", scanController.getStep());
        params_limits.setValue("delay", scanController.getSleepTime());
        XmlDataAdaptor params_trigger = params_scan2D.createChild("beam_trigger");
        params_trigger.setValue("on", scanController.getBeamTriggerState());
        params_trigger.setValue("delay", scanController.getBeamTriggerDelay());
        XmlDataAdaptor params_averg = params_scan2D.createChild("averaging");
        params_averg.setValue("on", avgCntr.isOn());
        params_averg.setValue("N", avgCntr.getAvgNumber());
        params_averg.setValue("delay", avgCntr.getTimeDelay());
        if (scanVariable.getChannel() != null) {
            XmlDataAdaptor scan_PV_name = scanPV_scan2D.createChild("PV");
            scan_PV_name.setValue("name", scanVariable.getChannelName());
        }
        if (scanVariable.getChannelRB() != null) {
            XmlDataAdaptor scan_PV_RB_name = scanPV_scan2D.createChild("PV_RB");
            scan_PV_RB_name.setValue("name", scanVariable.getChannelNameRB());
        }
        writeMeasuredValue(theDoc.scanStuff.FCT1PhaseMV, measurePVs_scan2D, FCT1PhaseName);
        writeMeasuredValue(theDoc.scanStuff.FCT2PhaseMV, measurePVs_scan2D, FCT2PhaseName);
        writeMeasuredValue(theDoc.scanStuff.FCT3PhaseMV, measurePVs_scan2D, FCT3PhaseName);
        writeMeasuredValue(theDoc.scanStuff.cavAmpRBMV, measurePVs_scan2D, cavAmpRBName);
        writeMeasuredValue(theDoc.scanStuff.BCMMV, measurePVs_scan2D, BCMName);
    }
