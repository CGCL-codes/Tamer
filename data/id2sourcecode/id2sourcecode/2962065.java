    protected void updateScanVariables(FCT fct1, FCT fct2, FCT fct3, RfCavity cav, CurrentMonitor bcm) {
        graphScan.removeAllGraphData();
        cavAmpPVName = (cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE)).getId();
        cavPhasePVName = cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE).getId();
        FCT1PhasePVName = fct1.getChannel(FCT.FCT_AVG_HANDLE).getId();
        FCT2PhasePVName = fct2.getChannel(FCT.FCT_AVG_HANDLE).getId();
        if (fct3 != null) FCT3PhasePVName = fct3.getChannel(FCT.FCT_AVG_HANDLE).getId();
        cavAmpRBPVName = cav.getChannel(RfCavity.CAV_AMP_AVG_HANDLE).getId();
        if (bcm != null) BCMPVName = bcm.getChannel(bcm.I_AVG_HANDLE).getId();
        System.out.println("pv = " + cavAmpRBPVName + "   " + BCMPVName);
        cavAmpRBChan = ChannelFactory.defaultFactory().getChannel(cavAmpRBPVName);
        if (bcm != null) BCMChan = ChannelFactory.defaultFactory().getChannel(BCMPVName);
        scanVariable.setChannel(cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE));
        scanVariableParameter.setChannel(cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE));
        FCT1PhaseMV.setChannel(fct1.getChannel(FCT.FCT_AVG_HANDLE));
        FCT2PhaseMV.setChannel(fct2.getChannel(FCT.FCT_AVG_HANDLE));
        if (fct3 != null) FCT3PhaseMV.setChannel(fct3.getChannel(FCT.FCT_AVG_HANDLE));
        cavAmpRBMV.setChannel(cavAmpRBChan);
        BCMMV.setChannel(BCMChan);
        connectChannels(fct1, fct2, fct3, cav);
        scanController.setScanVariable(scanVariable);
        scanController.setParamVariable(scanVariableParameter);
        setPVText();
    }
