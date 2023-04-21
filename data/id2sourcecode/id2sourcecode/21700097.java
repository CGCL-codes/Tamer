    private void connectChannels(BPM bpm1, BPM bpm2, RfCavity cav) {
        System.out.println("checking connections");
        connectionMap.clear();
        connectionMap.put(cavAmpPVName, new Boolean(false));
        connectionMap.put(cavPhasePVName, new Boolean(false));
        connectionMap.put(BPM1AmpPVName, new Boolean(false));
        connectionMap.put(BPM1PhasePVName, new Boolean(false));
        connectionMap.put(BPM2AmpPVName, new Boolean(false));
        connectionMap.put(BPM2PhasePVName, new Boolean(false));
        connectionMap.put(cavAmpRBPVName, new Boolean(false));
        connectionMap.put(BCMPVName, new Boolean(false));
        cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE).addConnectionListener(this);
        cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE).addConnectionListener(this);
        bpm1.getChannel(BPM.PHASE_AVG_HANDLE).addConnectionListener(this);
        bpm2.getChannel(BPM.PHASE_AVG_HANDLE).addConnectionListener(this);
        bpm1.getChannel(BPM.AMP_AVG_HANDLE).addConnectionListener(this);
        bpm2.getChannel(BPM.AMP_AVG_HANDLE).addConnectionListener(this);
        if (cavAmpRBChan != null) cavAmpRBChan.addConnectionListener(this);
        if (BCMChan != null) BCMChan.addConnectionListener(this);
        cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE).requestConnection();
        cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE).requestConnection();
        bpm1.getChannel(BPM.PHASE_AVG_HANDLE).requestConnection();
        bpm2.getChannel(BPM.PHASE_AVG_HANDLE).requestConnection();
        bpm1.getChannel(BPM.AMP_AVG_HANDLE).requestConnection();
        bpm2.getChannel(BPM.AMP_AVG_HANDLE).requestConnection();
        if (cavAmpRBChan != null) cavAmpRBChan.requestConnection();
        if (BCMChan != null) BCMChan.requestConnection();
        Channel.flushIO();
        int i = 0;
        int nDisconnects = 6;
        while (nDisconnects > 0 && i < 3) {
            try {
                Thread.currentThread().sleep(500);
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
        if (nDisconnects > 0) controller.dumpErr(nDisconnects + " PVs were not able to connect");
    }
