    private void connectChannels() {
        connectionMap.clear();
        int chanCounter = 0;
        connectionMap.put(longUpFctPV.getFCTVoltPVName(), new Boolean(false));
        connectionMap.put(longUpFctPV.getFCTSwitchPVName(), new Boolean(false));
        connectionMap.put(longUpFctPV.getFCTSwitchRBPVName(), new Boolean(false));
        connectionMap.put(longDownFctPV.getFCTVoltPVName(), new Boolean(false));
        connectionMap.put(longDownFctPV.getFCTSwitchPVName(), new Boolean(false));
        connectionMap.put(longDownFctPV.getFCTSwitchRBPVName(), new Boolean(false));
        if (theDoc.paramsStuff.useDoublePair) {
            if (theDoc.shortUpFCT != theDoc.longUpFCT && theDoc.shortUpFCT != theDoc.longDownFCT) {
                connectionMap.put(shortUpFctPV.getFCTVoltPVName(), new Boolean(false));
                connectionMap.put(shortUpFctPV.getFCTSwitchPVName(), new Boolean(false));
                connectionMap.put(shortUpFctPV.getFCTSwitchRBPVName(), new Boolean(false));
            }
            if (theDoc.shortDownFCT != theDoc.longUpFCT && theDoc.shortDownFCT != theDoc.longDownFCT) {
                connectionMap.put(shortDownFctPV.getFCTVoltPVName(), new Boolean(false));
                connectionMap.put(shortDownFctPV.getFCTSwitchPVName(), new Boolean(false));
                connectionMap.put(shortDownFctPV.getFCTSwitchRBPVName(), new Boolean(false));
            }
        }
        connectionMap.put(cavAmpPVName, new Boolean(false));
        connectionMap.put(cavPhasePVName, new Boolean(false));
        connectionMap.put(cavAmpPVRBName, new Boolean(false));
        connectionMap.put(cavPhasePVRBName, new Boolean(false));
        connectionMap.put(ampStrobePVName, new Boolean(false));
        connectionMap.put(phaseStrobePVName, new Boolean(false));
        connectionMap.put(cav1AmpRBPVName, new Boolean(false));
        connectionMap.put(cav1PhaseRBPVName, new Boolean(false));
        if (theDoc.theCavity.length == 2) {
            connectionMap.put(cav2AmpRBPVName, new Boolean(false));
            connectionMap.put(cav2PhaseRBPVName, new Boolean(false));
        }
        connectionMap.put(MPSStatusPVName, new Boolean(false));
        if (theDoc.theBCM != null) {
            connectionMap.put(sctPVName, new Boolean(false));
        }
        longUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).addConnectionListener(this);
        longUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).addConnectionListener(this);
        longUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).addConnectionListener(this);
        longDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).addConnectionListener(this);
        longDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).addConnectionListener(this);
        longDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).addConnectionListener(this);
        if (theDoc.paramsStuff.useDoublePair) {
            if (theDoc.shortUpFCT != theDoc.longUpFCT && theDoc.shortUpFCT != theDoc.longDownFCT) {
                shortUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).addConnectionListener(this);
                shortUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).addConnectionListener(this);
                shortUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).addConnectionListener(this);
            }
            if (theDoc.shortDownFCT != theDoc.longUpFCT && theDoc.shortDownFCT != theDoc.longDownFCT) {
                shortDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).addConnectionListener(this);
                shortDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).addConnectionListener(this);
                shortDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).addConnectionListener(this);
            }
        }
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_SET_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_SET_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_RB_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_RB_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).addConnectionListener(this);
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).addConnectionListener(this);
        if (theDoc.theCavity.length == 2) {
            theDoc.theCavity[1].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).addConnectionListener(this);
            theDoc.theCavity[1].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).addConnectionListener(this);
        }
        theDoc.theCavity[0].getChannel(RfCavity.MPS_STATUS_HANDLE).addConnectionListener(this);
        if (theDoc.theBCM != null) {
            theDoc.theBCM.getChannel(CurrentMonitor.I_AVG_HANDLE).addConnectionListener(this);
        }
        longUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).requestConnection();
        chanCounter++;
        longUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).requestConnection();
        chanCounter++;
        longUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).requestConnection();
        chanCounter++;
        longDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).requestConnection();
        chanCounter++;
        longDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).requestConnection();
        chanCounter++;
        longDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).requestConnection();
        chanCounter++;
        if (theDoc.paramsStuff.useDoublePair) {
            if (theDoc.shortUpFCT != theDoc.longUpFCT && theDoc.shortUpFCT != theDoc.longDownFCT) {
                shortUpFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).requestConnection();
                shortUpFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).requestConnection();
                chanCounter++;
                shortUpFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).requestConnection();
                chanCounter++;
            }
            if (theDoc.shortDownFCT != theDoc.longUpFCT && theDoc.shortDownFCT != theDoc.longDownFCT) {
                shortDownFctPV.getFCT().getChannel(FCT.VOLT_AVG_HANDLE).requestConnection();
                chanCounter++;
                shortDownFctPV.getFCT().getChannel(FCT.PHASE_SWITCH_HANDLE).requestConnection();
                chanCounter++;
                shortDownFctPV.getFCT().getChannel(FCT.SWITCH_RB_HANDLE).requestConnection();
                chanCounter++;
            }
        }
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_SET_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_SET_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_RB_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_RB_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_STROBE_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_STROBE_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).requestConnection();
        chanCounter++;
        theDoc.theCavity[0].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).requestConnection();
        chanCounter++;
        if (theDoc.theCavity.length == 2) {
            theDoc.theCavity[1].getChannel(RfCavity.CAV_AMP_AVG_HANDLE).requestConnection();
            chanCounter++;
            theDoc.theCavity[1].getChannel(RfCavity.CAV_PHASE_AVG_HANDLE).requestConnection();
            chanCounter++;
        }
        theDoc.theCavity[0].getChannel(RfCavity.MPS_STATUS_HANDLE).requestConnection();
        chanCounter++;
        if (theDoc.theBCM != null) {
            theDoc.theBCM.getChannel(CurrentMonitor.I_AVG_HANDLE).requestConnection();
            chanCounter++;
        }
        Channel.flushIO();
        int i = 0;
        int nDisconnects = chanCounter;
        while (nDisconnects > 0 && i < (chanCounter)) {
            try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted during connection check");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            nDisconnects = 0;
            Set set = connectionMap.entrySet();
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                Boolean tf = (Boolean) me.getValue();
                if (!(tf.booleanValue())) nDisconnects++;
            }
            i++;
        }
        if (nDisconnects > 0) {
            Toolkit.getDefaultToolkit().beep();
            theDoc.myWindow().errorText.setText((new Integer(nDisconnects)).toString() + " PVs were not able to connect");
            System.out.println(nDisconnects + " PVs were not able to connect");
        }
    }
