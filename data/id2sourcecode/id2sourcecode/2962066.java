    private void connectChannels(FCT fct1, FCT fct2, FCT fct3, RfCavity cav) {
        connectionMap.clear();
        connectionMap.put(cavAmpPVName, new Boolean(false));
        connectionMap.put(cavPhasePVName, new Boolean(false));
        connectionMap.put(FCT1PhasePVName, new Boolean(false));
        connectionMap.put(FCT2PhasePVName, new Boolean(false));
        if (fct3 != null) connectionMap.put(FCT3PhasePVName, new Boolean(false));
        connectionMap.put(cavAmpRBPVName, new Boolean(false));
        connectionMap.put(BCMPVName, new Boolean(false));
        cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE).addConnectionListener(this);
        cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE).addConnectionListener(this);
        fct1.getChannel(FCT.FCT_AVG_HANDLE).addConnectionListener(this);
        fct2.getChannel(FCT.FCT_AVG_HANDLE).addConnectionListener(this);
        if (fct3 != null) fct3.getChannel(FCT.FCT_AVG_HANDLE).addConnectionListener(this);
        if (cavAmpRBChan != null) cavAmpRBChan.addConnectionListener(this);
        if (BCMChan != null) BCMChan.addConnectionListener(this);
        cav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE).requestConnection();
        cav.getChannel(RfCavity.CAV_AMP_SET_HANDLE).requestConnection();
        fct1.getChannel(FCT.FCT_AVG_HANDLE).requestConnection();
        fct2.getChannel(FCT.FCT_AVG_HANDLE).requestConnection();
        if (fct3 != null) fct3.getChannel(FCT.FCT_AVG_HANDLE).requestConnection();
        if (cavAmpRBChan != null) cavAmpRBChan.requestConnection();
        if (BCMChan != null) BCMChan.requestConnection();
        int i = 0;
        int nDisconnects = 6;
        while (nDisconnects > 0 && i < 5) {
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
