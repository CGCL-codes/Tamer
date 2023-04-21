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
        if (theDoc.phiUpFCT == null) {
            System.out.println("updatescanvariables>, theDoc.phiUpFCT = null ");
        } else {
            System.out.println("updatescanvariables>, theDoc.phiUpFCT = " + theDoc.phiUpFCT);
        }
        if (theDoc.phiDownFCT == null) {
            System.out.println("updatescanvariables>, theDoc.phiDownFCT = null ");
        } else {
            System.out.println("updatescanvariables>, theDoc.phiDownFCT = " + theDoc.phiDownFCT);
        }
        if (theDoc.phiUpFCT != null && theDoc.phiDownFCT != null) {
            System.out.println("updatescanvariables> phiUpFctPV = " + phiUpFctPV);
            System.out.println("updatescanvariables> theDoc.phiUpFCT = " + theDoc.phiUpFCT);
            phiUpFctPV.setFctPvName(theDoc.phiUpFCT);
            phiDownFctPV.setFctPvName(theDoc.phiDownFCT);
        } else {
            System.out.println("updatescanvariables> theDoc.phiUpFCT == null or theDoc.phiDownFCT == null");
        }
        System.out.println("updatescanvariables> *** going to initMeasuredValueV");
        initMeasuredValueV();
        System.out.println("updatescanvariables> *** end initMeasuredValueV");
        scanVariable.setChannel(cav[0].getChannel(RfCavity.CAV_PHASE_SET_HANDLE));
        scanVariable.setChannelRB(cav[0].getChannel(RfCavity.CAV_PHASE_RB_HANDLE));
        scanVariable.setStrobeChan(cav[0].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE));
        scanVariableParameter.setChannel(cav[0].getChannel(RfCavity.CAV_AMP_SET_HANDLE));
        scanVariableParameter.setChannelRB(cav[0].getChannel(RfCavity.CAV_AMP_RB_HANDLE));
        scanVariableParameter.setStrobeChan(cav[0].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE));
        int FCTNumber = 0;
        ArrayList<Channel> phSwitchChan = new ArrayList<Channel>();
        ArrayList<Channel> phSwitchChanRB = new ArrayList<Channel>();
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
        System.out.println("updateScanVariables... theDoc.longUpFCT = " + theDoc.longUpFCT);
        if (phiUpFCTVoltMVNorm == null) {
            System.out.println("updatescanvariables> before theDoc.phiUpFCT == ... phiUpFCTVoltMVNorm == null");
        } else {
            System.out.println("updatescanvariables> before theDoc.phiUpFCT == ... phiUpFCTVoltMVNorm = " + phiUpFCTVoltMVNorm);
        }
        System.out.println("theDoc.paramsStuff.useDoublePair = " + theDoc.paramsStuff.useDoublePair);
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
        if (theDoc.phiUpFCT == theDoc.longUpFCT) {
            System.out.println("updatescanvariables. phiUpFCT == longUpFCT");
            if (longUpFCTVoltMVNorm == null) {
                System.out.println("longUpFCTVoltMVNorm == null");
            } else {
                System.out.println("longUpFCTVoltMVNorm = " + longUpFCTVoltMVNorm);
            }
            phiUpFCTVoltMVNorm = longUpFCTVoltMVNorm;
            phiUpFCTVoltMVInv = longUpFCTVoltMVInv;
        } else if (theDoc.phiUpFCT == theDoc.longDownFCT) {
            System.out.println("updatescanvariables. phiUpFCT == longDownFCT");
            if (longDownFCTVoltMVNorm == null) {
                System.out.println("longDownFCTVoltMVNorm == null");
            } else {
                System.out.println("longDownFCTVoltMVNorm = " + longDownFCTVoltMVNorm);
            }
            phiUpFCTVoltMVNorm = longDownFCTVoltMVNorm;
            phiUpFCTVoltMVInv = longDownFCTVoltMVInv;
        } else if (theDoc.paramsStuff.useDoublePair && (theDoc.phiUpFCT == theDoc.shortUpFCT)) {
            System.out.println("updatescanvariables. phiUpFCT == shortUpFCT");
            if (shortUpFCTVoltMVNorm == null) {
                System.out.println("shortUpFCTVoltMVNorm == null");
            } else {
                System.out.println("shortUpFCTVoltMVNorm = " + shortUpFCTVoltMVNorm);
            }
            phiUpFCTVoltMVNorm = shortUpFCTVoltMVNorm;
            phiUpFCTVoltMVInv = shortUpFCTVoltMVInv;
        } else if (theDoc.paramsStuff.useDoublePair && (theDoc.phiUpFCT == theDoc.shortDownFCT)) {
            System.out.println("updatescanvariables. phiUpFCT == shortDownFCT");
            if (shortDownFCTVoltMVNorm == null) {
                System.out.println("shortDownFCTVoltMVNorm == null");
            } else {
                System.out.println("shortDownFCTVoltMVNorm = " + shortDownFCTVoltMVNorm);
            }
            phiUpFCTVoltMVNorm = shortDownFCTVoltMVNorm;
            phiUpFCTVoltMVInv = shortDownFCTVoltMVInv;
        } else {
            System.out.println("updatescanvariables. phiUpFCT != anyFCT");
            phiUpFCTVoltMVNorm.setChannel(phiUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
            phiUpFCTVoltMVInv.setChannel(phiUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
            FCTNumber++;
            phSwitchChan.add(phiUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
            phSwitchChanRB.add(phiUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
        }
        if (phiUpFCTVoltMVNorm == null) {
            System.out.println("updatescanvariables> after theDoc.phiUpFCT == ... phiUpFCTVoltMVNorm == null");
        } else {
            System.out.println("updatescanvariables> after theDoc.phiUpFCT == ... phiUpFCTVoltMVNorm = " + phiUpFCTVoltMVNorm);
        }
        if (theDoc.phiDownFCT == theDoc.longUpFCT) {
            phiDownFCTVoltMVNorm = longUpFCTVoltMVNorm;
            phiDownFCTVoltMVInv = longUpFCTVoltMVInv;
        } else if (theDoc.phiDownFCT == theDoc.longDownFCT) {
            phiDownFCTVoltMVNorm = longDownFCTVoltMVNorm;
            phiDownFCTVoltMVInv = longDownFCTVoltMVInv;
        } else if (theDoc.paramsStuff.useDoublePair && (theDoc.phiDownFCT == theDoc.shortUpFCT)) {
            phiDownFCTVoltMVNorm = shortUpFCTVoltMVNorm;
            phiDownFCTVoltMVInv = shortUpFCTVoltMVInv;
        } else if (theDoc.paramsStuff.useDoublePair && (theDoc.phiDownFCT == theDoc.shortDownFCT)) {
            phiDownFCTVoltMVNorm = shortDownFCTVoltMVNorm;
            phiDownFCTVoltMVInv = shortDownFCTVoltMVInv;
        } else {
            phiDownFCTVoltMVNorm.setChannel(phiDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
            phiDownFCTVoltMVInv.setChannel(phiDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE));
            FCTNumber++;
            phSwitchChan.add(phiDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE));
            phSwitchChanRB.add(phiDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE));
        }
        if (phiDownFCTVoltMVNorm == null) {
            System.out.println("updatescanvariables> after theDoc.phiDownFCT == ... phiDownFCTVoltMVNorm == null");
        } else {
            System.out.println("updatescanvariables> after theDoc.phiDownFCT == ... phiDownFCTVoltMVNorm = " + phiDownFCTVoltMVNorm);
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
