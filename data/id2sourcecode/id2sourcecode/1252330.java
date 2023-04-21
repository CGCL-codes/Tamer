    protected void updateScanVariables(BPM bpm1, BPM[] bpm2, BPM bpm3, HDipoleCorr hDipoleCorr, VDipoleCorr vDipoleCorr, Electromagnet quadMagnet, CurrentMonitor bcm) {
        graphScanX.removeAllGraphData();
        graphScanY.removeAllGraphData();
        if (hDipoleCorr != null) {
            BPM1XPosPVName = bpm1.getChannel(BPM.X_AVG_HANDLE).getId();
            for (int i = 0; i < bpm2.length; i++) {
                BPM2XPosPVName[i] = bpm2[i].getChannel(BPM.X_AVG_HANDLE).getId();
            }
            if (bpm3 != null) BPM3XPosPVName = bpm3.getChannel(BPM.X_AVG_HANDLE).getId();
            QuadMagXSetPVName = quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).getId();
            QuadMagXSetRBPVName = quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).getId();
            HDipoleCorrPVName = hDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).getId();
            HDipoleCorrRBPVName = hDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).getId();
        }
        if (vDipoleCorr != null) {
            BPM1YPosPVName = bpm1.getChannel(BPM.Y_AVG_HANDLE).getId();
            for (int i = 0; i < bpm2.length; i++) {
                BPM2YPosPVName[i] = bpm2[i].getChannel(BPM.Y_AVG_HANDLE).getId();
            }
            if (bpm3 != null) BPM3YPosPVName = bpm3.getChannel(BPM.Y_AVG_HANDLE).getId();
            QuadMagYSetPVName = quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).getId();
            QuadMagYSetRBPVName = quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).getId();
            VDipoleCorrPVName = vDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).getId();
            VDipoleCorrRBPVName = vDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).getId();
        }
        if (hDipoleCorr != null) {
            scanVariableCorrX.setChannel(hDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            scanVariableCorrX.setChannelRB(hDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            scanVariableQuadX.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            scanVariableQuadX.setChannelRB(quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            BPM1XPosMV.setChannel(bpm1.getChannel(BPM.X_AVG_HANDLE));
            for (int i = 0; i < bpm2.length; i++) {
                BPM2XPosMV[i].setChannel(bpm2[i].getChannel(BPM.X_AVG_HANDLE));
            }
            if (bpm3 != null) BPM3XPosMV.setChannel(bpm3.getChannel(BPM.X_AVG_HANDLE));
            QuadMagXSetMV.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            QuadMagXSetRBMV.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            HDipoleCorrMV.setChannel(hDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            HDipoleCorrRBMV.setChannel(hDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
        }
        if (vDipoleCorr != null) {
            scanVariableCorrY.setChannel(vDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            scanVariableCorrY.setChannelRB(vDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            scanVariableQuadY.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            scanVariableQuadY.setChannelRB(quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            BPM1YPosMV.setChannel(bpm1.getChannel(BPM.Y_AVG_HANDLE));
            for (int i = 0; i < bpm2.length; i++) {
                BPM2YPosMV[i].setChannel(bpm2[i].getChannel(BPM.Y_AVG_HANDLE));
            }
            if (bpm3 != null) BPM3YPosMV.setChannel(bpm3.getChannel(BPM.Y_AVG_HANDLE));
            QuadMagYSetMV.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            QuadMagYSetRBMV.setChannel(quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
            VDipoleCorrMV.setChannel(vDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            VDipoleCorrRBMV.setChannel(vDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE));
        }
        if (bcm != null) {
            BCMXPvName = bcm.getChannel(bcm.I_AVG_HANDLE).getId();
            BCMYPvName = BCMXPvName;
            BCMMVX.setChannel(bcm.getChannel(CurrentMonitor.I_AVG_HANDLE));
            BCMMVY.setChannel(bcm.getChannel(CurrentMonitor.I_AVG_HANDLE));
        }
        Channel.flushIO();
        connectChannels(bpm1, bpm2, bpm3, hDipoleCorr, vDipoleCorr, quadMagnet, bcm);
        if (vDipoleCorr != null) {
            scanControllerY.setScanVariable(scanVariableQuadY);
            scanControllerY.setParamVariable(scanVariableCorrY);
        }
        if (hDipoleCorr != null) {
            scanControllerX.setScanVariable(scanVariableQuadX);
            scanControllerX.setParamVariable(scanVariableCorrX);
        }
        setPVText(bpm1, bpm2, bpm3, hDipoleCorr, vDipoleCorr, quadMagnet);
    }
