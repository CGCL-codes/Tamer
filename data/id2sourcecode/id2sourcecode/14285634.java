    private void identifyScanRF() {
        scanRF = null;
        String scanRec;
        String scanRecReset1;
        String scanRecReset2;
        int bit = 0;
        if (!isBeamOn) {
            scanRec = prop.getRFDTLScanRecord();
            scanRecReset1 = prop.getRFSDTLScanRecord();
            scanRecReset2 = prop.getRFBCQDScanRecord();
        } else {
            scanRec = prop.getRFDTLScanRecord();
            scanRecReset1 = prop.getRFSDTLScanRecord();
            scanRecReset2 = prop.getRFBCQDScanRecord();
            for (int irf = currentRFManagers.size() - 1; irf >= 0; irf--) {
                RFManager RF = currentRFManagers.get(irf);
                boolean RFampok = RF.checkProperAmplitude();
                boolean RFon = RF.checkRFOn();
                boolean RFsync = RF.checkBeamSynchronized();
                boolean RFscan = RF.checkScanned();
                if (RFampok & RFon && RFsync && RFscan) {
                    scanRF = RF;
                    RFCalib calib = RF.getCalib();
                    int bitMode = calib.getTimingRecordBit();
                    bit = bitMode % 100;
                    if (bitMode < 100) {
                        scanRec = prop.getRFDTLScanRecord();
                        scanRecReset1 = prop.getRFSDTLScanRecord();
                        scanRecReset2 = prop.getRFBCQDScanRecord();
                    } else if (bitMode < 200) {
                        scanRec = prop.getRFSDTLScanRecord();
                        scanRecReset1 = prop.getRFDTLScanRecord();
                        scanRecReset2 = prop.getRFBCQDScanRecord();
                    } else {
                        scanRec = prop.getRFBCQDScanRecord();
                        scanRecReset1 = prop.getRFDTLScanRecord();
                        scanRecReset2 = prop.getRFSDTLScanRecord();
                    }
                    break;
                }
            }
            if (RFScanChannel == null) {
                RFScanChannel = ChannelFactory.defaultFactory().getChannel(scanRec);
            }
            if (RFScanChannelR1 == null) {
                RFScanChannelR1 = ChannelFactory.defaultFactory().getChannel(scanRecReset1);
            }
            if (RFScanChannelR2 == null) {
                RFScanChannelR2 = ChannelFactory.defaultFactory().getChannel(scanRecReset2);
            }
            if (caputFlag) {
                CaMonitorScalar.setChannel(RFScanChannel, bit);
                CaMonitorScalar.setChannel(RFScanChannelR1, 0);
                CaMonitorScalar.setChannel(RFScanChannelR2, 0);
            }
        }
    }
