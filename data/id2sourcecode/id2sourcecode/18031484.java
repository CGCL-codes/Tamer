    public synchronized void addBPMs(java.util.List bpmList) {
        if (goodBPMs != null) goodBPMs.clear(); else goodBPMs = new ArrayList();
        if (badBPMs != null) badBPMs.clear(); else badBPMs = new ArrayList();
        ArrayList pvs = new ArrayList(2 * bpmList.size());
        Iterator it = bpmList.iterator();
        while (it.hasNext()) {
            BPM bpm = (BPM) it.next();
            String xAvg = bpm.getId() + ":" + BPM.X_AVG_HANDLE;
            String yAvg = bpm.getId() + ":" + BPM.Y_AVG_HANDLE;
            pvs.add(xAvg);
            pvs.add(yAvg);
            bpm.getChannel(BPM.X_AVG_HANDLE).connect_async();
            bpm.getChannel(BPM.Y_AVG_HANDLE).connect_async();
        }
        ConnectionChecker cc = new ConnectionChecker(pvs);
        cc.checkThem();
        if (!cc.getGoodPVs().isEmpty()) goodBPMs.addAll(cc.getGoodPVs());
        if (!cc.getBadPVs().isEmpty()) {
            badBPMs.addAll(cc.getBadPVs());
            noBadBPMs = false;
        } else noBadBPMs = true;
        Iterator itGood = goodBPMs.iterator();
        while (itGood.hasNext()) {
            String pv = (String) itGood.next();
            addChannel(pv);
        }
    }
