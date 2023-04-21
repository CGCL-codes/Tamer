    private void connectChannels(BPM bpm1, BPM[] bpm2, BPM bpm3, HDipoleCorr hDipoleCorr, VDipoleCorr vDipoleCorr, Electromagnet quadMagnet, CurrentMonitor bcm) {
        connectionMap.clear();
        if (hDipoleCorr != null) {
            connectionMap.put(BPM1XPosPVName, new Boolean(false));
            for (int i = 0; i < bpm2.length; i++) {
                connectionMap.put(BPM2XPosPVName[i], new Boolean(false));
            }
            if (bpm3 != null) connectionMap.put(BPM3XPosPVName, new Boolean(false));
            connectionMap.put(QuadMagXSetPVName, new Boolean(false));
            connectionMap.put(QuadMagXSetRBPVName, new Boolean(false));
            connectionMap.put(HDipoleCorrPVName, new Boolean(false));
            connectionMap.put(HDipoleCorrRBPVName, new Boolean(false));
        }
        if (vDipoleCorr != null) {
            connectionMap.put(BPM1YPosPVName, new Boolean(false));
            for (int i = 0; i < bpm2.length; i++) {
                connectionMap.put(BPM2YPosPVName[i], new Boolean(false));
            }
            if (bpm3 != null) connectionMap.put(BPM3YPosPVName, new Boolean(false));
            connectionMap.put(QuadMagYSetPVName, new Boolean(false));
            connectionMap.put(QuadMagYSetRBPVName, new Boolean(false));
            connectionMap.put(VDipoleCorrPVName, new Boolean(false));
            connectionMap.put(VDipoleCorrRBPVName, new Boolean(false));
        }
        if (hDipoleCorr != null && bcm != null) {
            connectionMap.put(BCMXPvName, new Boolean(false));
        }
        if (vDipoleCorr != null && bcm != null) {
            connectionMap.put(BCMYPvName, new Boolean(false));
        }
        if (hDipoleCorr != null) {
            bpm1.getChannel(BPM.X_AVG_HANDLE).addConnectionListener(this);
            for (int i = 0; i < bpm2.length; i++) {
                bpm2[i].getChannel(BPM.X_AVG_HANDLE).addConnectionListener(this);
            }
            if (bpm3 != null) bpm3.getChannel(BPM.X_AVG_HANDLE).addConnectionListener(this);
            quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).addConnectionListener(this);
            quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).addConnectionListener(this);
            hDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).addConnectionListener(this);
            hDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).addConnectionListener(this);
        }
        if (vDipoleCorr != null) {
            bpm1.getChannel(BPM.Y_AVG_HANDLE).addConnectionListener(this);
            for (int i = 0; i < bpm2.length; i++) {
                bpm2[i].getChannel(BPM.Y_AVG_HANDLE).addConnectionListener(this);
            }
            if (bpm3 != null) bpm3.getChannel(BPM.Y_AVG_HANDLE).addConnectionListener(this);
            quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).addConnectionListener(this);
            quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).addConnectionListener(this);
            vDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).addConnectionListener(this);
            vDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).addConnectionListener(this);
        }
        if (hDipoleCorr != null) {
            bpm1.getChannel(BPM.X_AVG_HANDLE).requestConnection();
            for (int i = 0; i < bpm2.length; i++) {
                bpm2[i].getChannel(BPM.X_AVG_HANDLE).requestConnection();
            }
            if (bpm3 != null) bpm3.getChannel(BPM.X_AVG_HANDLE).requestConnection();
            quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).requestConnection();
            quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).requestConnection();
            hDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).requestConnection();
            hDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).requestConnection();
        }
        if (vDipoleCorr != null) {
            bpm1.getChannel(BPM.Y_AVG_HANDLE).requestConnection();
            for (int i = 0; i < bpm2.length; i++) {
                bpm2[i].getChannel(BPM.Y_AVG_HANDLE).requestConnection();
            }
            if (bpm3 != null) bpm3.getChannel(BPM.Y_AVG_HANDLE).requestConnection();
            quadMagnet.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).requestConnection();
            quadMagnet.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).requestConnection();
            vDipoleCorr.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).requestConnection();
            vDipoleCorr.getChannel(MagnetMainSupply.FIELD_RB_HANDLE).requestConnection();
        }
        if (bcm != null) {
            (bcm.getChannel(CurrentMonitor.I_AVG_HANDLE)).addConnectionListener(this);
            (bcm.getChannel(CurrentMonitor.I_AVG_HANDLE)).requestConnection();
        }
        Channel.flushIO();
        int i = 0;
        int nDisconnects = connectionMap.size();
        int totalDisconnect = nDisconnects - 1;
        while (nDisconnects > 0 && i < totalDisconnect) {
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
        if (hDipoleCorr == null) {
            scanControllerX.setStartButtonEnabled(false);
        } else {
            scanControllerX.setStartButtonEnabled(true);
        }
        if (vDipoleCorr == null) {
            scanControllerY.setStartButtonEnabled(false);
        } else {
            scanControllerY.setStartButtonEnabled(true);
        }
    }
