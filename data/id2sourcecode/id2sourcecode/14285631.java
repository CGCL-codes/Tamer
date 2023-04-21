    private void identifyBeam() {
        beamDestination = (int) beamMode.getValue();
        System.out.println("beamDestination = " + beamDestination);
        curLEBT = SCTLEBT.getValue();
        curOnMEBT1 = SCTOnMEBT1.getValue();
        curOffMEBT1 = SCTOffMEBT1.getValue();
        cur0Dump = SCT0Dump.getValue();
        cur30Dump = SCT30Dump.getValue();
        cur90Dump = SCT90Dump.getValue();
        cur100Dump = SCT100Dump.getValue();
        curL3BT = SCTL3BT.getValue();
        double curTh = prop.getSCTCurThreshold();
        currentBeamMode = getBeamDestinationTag();
        switch(beamDestination) {
            case beamToNOWHERE:
            case beamToLEBT:
                currentRFManagers = RFManagersMEBT1;
                curCurrent = 0;
                curSCT = null;
                isBeamOn = false;
                break;
            case beamToMEBT1:
                currentRFManagers = RFManagersMEBT1;
                curCurrent = curOnMEBT1;
                curSCT = SCTOnMEBT1.getChannelName();
                if (curCurrent >= curTh) {
                    isBeamOn = true;
                } else {
                    isBeamOn = false;
                }
                break;
            case beamTo0Dump:
            case beamTo30Dump:
            case beamTo90Dump:
            case beamTo100Dump:
            case beamToH0Dump:
            case beamTo3NDumpDC:
            case beamTo3NDumpAC:
            case beamToMLFTarget:
            case beamToMRInjDump:
            case beamToMRExtDump:
            case beamToKTarget:
            case beamToNTarget:
            case beamToKNTarget:
            case beamToMLF_MRInj:
            case beamToMLF_MRExt:
            case beamToMLF_K:
            case beamToMLF_N:
            case beamToMLF_KN:
                currentRFManagers = RFManagersL3BT;
                curCurrent = curOnMEBT1;
                curSCT = SCTOnMEBT1.getChannelName();
                if (curCurrent >= curTh) {
                    isBeamOn = true;
                } else {
                    isBeamOn = false;
                }
                break;
            default:
                currentRFManagers = RFManagersL3BT;
                curCurrent = curOnMEBT1;
                curSCT = SCTOnMEBT1.getChannelName();
                if (curCurrent >= curTh) {
                    isBeamOn = true;
                } else {
                    isBeamOn = false;
                }
                break;
        }
        calculateRCSRFEnergy(RCSRF.getValue());
        if (caputFlag) {
            if (eneRCSRFInjChannel == null) {
                eneRCSRFInjChannel = ChannelFactory.defaultFactory().getChannel(prop.getRCSEnergyInjRecord());
            }
            CaMonitorScalar.setChannel(eneRCSRFInjChannel, eneRCSRFInj);
            if (eneRCSRFExtChannel == null) {
                eneRCSRFExtChannel = ChannelFactory.defaultFactory().getChannel(prop.getRCSEnergyExtRecord());
            }
            CaMonitorScalar.setChannel(eneRCSRFExtChannel, eneRCSRFExt);
            if (chopperFreqChannel == null) {
                chopperFreqChannel = ChannelFactory.defaultFactory().getChannel(prop.getChopperFreqRecord());
            }
            CaMonitorScalar.setChannel(chopperFreqChannel, freqRCSRFInj);
        }
    }
