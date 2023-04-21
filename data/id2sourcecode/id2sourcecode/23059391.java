    protected void updateScanVariables(RfCavity[] cav, CurrentMonitor sct) {
        graphScan.removeAllGraphData();
        String cav1AmpPVName = cav[0].getChannel(RfCavity.CAV_AMP_SET_HANDLE).getId();
        if (cav.length == 2) {
            String cav2AmpPVName = cav[1].getChannel(RfCavity.CAV_AMP_SET_HANDLE).getId();
            if (!cav1AmpPVName.equals(cav2AmpPVName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for Cavity amplitude set.");
                return;
            }
        }
        cavAmpPVName = cav1AmpPVName;
        String cav1AmpPVRBName = cav[0].getChannel(RfCavity.CAV_AMP_RB_HANDLE).getId();
        if (cav.length == 2) {
            String cav2AmpPVRBName = cav[1].getChannel(RfCavity.CAV_AMP_RB_HANDLE).getId();
            if (!cav1AmpPVRBName.equals(cav2AmpPVRBName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for Cavity amplitude setting readback.");
                return;
            }
        }
        cavAmpPVRBName = cav1AmpPVRBName;
        String cav1PhasePVName = cav[0].getChannel(RfCavity.CAV_PHASE_SET_HANDLE).getId();
        if (cav.length == 2) {
            String cav2PhasePVName = cav[1].getChannel(RfCavity.CAV_PHASE_SET_HANDLE).getId();
            if (!cav1PhasePVName.equals(cav2PhasePVName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for Cavity phase set.");
                return;
            }
        }
        cavPhasePVName = cav1PhasePVName;
        String cav1PhasePVRBName = cav[0].getChannel(RfCavity.CAV_PHASE_RB_HANDLE).getId();
        if (cav.length == 2) {
            String cav2PhasePVRBName = cav[1].getChannel(RfCavity.CAV_PHASE_RB_HANDLE).getId();
            if (!cav1PhasePVRBName.equals(cav2PhasePVRBName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for Cavity phase setting readback.");
                return;
            }
        }
        cavPhasePVRBName = cav1PhasePVRBName;
        String amp1StrobePVName = cav[0].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE).getId();
        if (cav.length == 2) {
            String amp2StrobePVName = cav[1].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE).getId();
            if (!amp1StrobePVName.equals(amp2StrobePVName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for strobe signal of Cavity amplitude set.");
                return;
            }
        }
        ampStrobePVName = amp1StrobePVName;
        String phase1StrobePVName = cav[0].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE).getId();
        if (cav.length == 2) {
            String phase2StrobePVName = cav[1].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE).getId();
            if (!phase1StrobePVName.equals(phase2StrobePVName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for strobe signal of Cavity phase set.");
                return;
            }
        }
        phaseStrobePVName = phase1StrobePVName;
        cav1AmpRBPVName = cav[0].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).getId();
        cav1PhaseRBPVName = cav[0].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).getId();
        if (cav.length == 2) {
            cav2AmpRBPVName = cav[1].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).getId();
            cav2PhaseRBPVName = cav[1].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).getId();
        }
        String cav1MPSPVName = cav[0].getChannel(RfCavity.MPS_STATUS_HANDLE).getId();
        if (cav.length == 2) {
            String cav2MPSPVName = cav[1].getChannel(RfCavity.MPS_STATUS_HANDLE).getId();
            if (!cav1MPSPVName.equals(cav2MPSPVName)) {
                JOptionPane.showMessageDialog(theDoc.myWindow(), "The EPICS Channel name is mismatched for MPS status checking.");
                return;
            }
        }
        MPSStatusPVName = cav1MPSPVName;
        if (sct != null) {
            sctPVName = sct.getChannel(CurrentMonitor.I_AVG_HANDLE).getId();
        }
        longUpFctPV.setFctPvName(theDoc.longUpFCT);
        longDownFctPV.setFctPvName(theDoc.longDownFCT);
        if (theDoc.paramsStuff.useDoublePair && theDoc.shortUpFCT != null && theDoc.shortDownFCT != null) {
            if (theDoc.shortUpFCT == theDoc.longUpFCT) {
                shortUpFctPV = longUpFctPV;
            } else if (theDoc.shortUpFCT == theDoc.longDownFCT) {
                shortUpFctPV = longDownFctPV;
            } else {
                shortUpFctPV.setFctPvName(theDoc.shortUpFCT);
            }
            if (theDoc.shortDownFCT == theDoc.longUpFCT) {
                shortDownFctPV = longUpFctPV;
            } else if (theDoc.shortDownFCT == theDoc.longDownFCT) {
                shortDownFctPV = longDownFctPV;
            } else {
                shortDownFctPV.setFctPvName(theDoc.shortDownFCT);
            }
        }
        initMeasuredValueV();
        scanVariable.setChannel(cav[0].getChannel(RfCavity.CAV_PHASE_SET_HANDLE));
        scanVariable.setChannelRB(cav[0].getChannel(RfCavity.CAV_PHASE_RB_HANDLE));
        scanVariable.setStrobeChan(cav[0].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE));
        scanVariableParameter.setChannel(cav[0].getChannel(RfCavity.CAV_AMP_SET_HANDLE));
        scanVariableParameter.setChannelRB(cav[0].getChannel(RfCavity.CAV_AMP_RB_HANDLE));
        scanVariableParameter.setStrobeChan(cav[0].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE));
        int FCTNumber = 0;
        ArrayList<Channel> phSwitchChan = new ArrayList();
        ArrayList<Channel> phSwitchChanRB = new ArrayList();
        longUpFCTVoltMVNorm.setChannel(longUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
        longUpFCTVoltMVInv.setChannel(longUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
        FCTNumber++;
        phSwitchChan.add(longUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
        phSwitchChanRB.add(longUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
        longDownFCTVoltMVNorm.setChannel(longDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
        longDownFCTVoltMVInv.setChannel(longDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
        FCTNumber++;
        phSwitchChan.add(longDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
        phSwitchChanRB.add(longDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
        if (theDoc.paramsStuff.useDoublePair) {
            if (theDoc.shortUpFCT == theDoc.longUpFCT) {
                shortUpFCTVoltMVNorm = longUpFCTVoltMVNorm;
                shortUpFCTVoltMVInv = longUpFCTVoltMVInv;
            } else if (theDoc.shortUpFCT == theDoc.longDownFCT) {
                shortUpFCTVoltMVNorm = longDownFCTVoltMVNorm;
                shortUpFCTVoltMVInv = longDownFCTVoltMVInv;
            } else {
                shortUpFCTVoltMVNorm.setChannel(shortUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
                shortUpFCTVoltMVInv.setChannel(shortUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
                FCTNumber++;
                phSwitchChan.add(shortUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
                phSwitchChanRB.add(shortUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
            }
            if (theDoc.shortDownFCT == theDoc.longUpFCT) {
                shortDownFCTVoltMVNorm = longUpFCTVoltMVNorm;
                shortDownFCTVoltMVInv = longUpFCTVoltMVInv;
            } else if (theDoc.shortDownFCT == theDoc.longDownFCT) {
                shortDownFCTVoltMVNorm = longDownFCTVoltMVNorm;
                shortDownFCTVoltMVInv = longDownFCTVoltMVInv;
            } else {
                shortDownFCTVoltMVNorm.setChannel(shortDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
                shortDownFCTVoltMVInv.setChannel(shortDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
                FCTNumber++;
                phSwitchChan.add(shortDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
                phSwitchChanRB.add(shortDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
            }
        }
        scanController.setPhaseSwitcher(FCTNumber, phSwitchChan, phSwitchChanRB);
        cav1AmpRBMV.setChannel(cav[0].getChannel(RfCavity.CAV_AMP_AVG_HANDLE));
        cav1PhaseRBMV.setChannel(cav[0].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE));
        if (theDoc.theCavity.length == 2) {
            cav2AmpRBMV.setChannel(cav[1].getChannel(RfCavity.CAV_AMP_AVG_HANDLE));
            cav2PhaseRBMV.setChannel(cav[1].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE));
        }
        if (theDoc.theBCM != null) {
            sctMV.setChannel(sct.getChannel(CurrentMonitor.I_AVG_HANDLE));
        }
        mpsStatusMV.setChannel(cav[0].getChannel(RfCavity.MPS_STATUS_HANDLE));
        Channel.flushIO();
        connectChannels();
        scanController.setScanVariable(scanVariable);
        scanController.setParamVariable(scanVariableParameter);
        setPVText();
    }
