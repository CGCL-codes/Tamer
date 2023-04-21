    private void updateBPM() {
        if (simulationType != SimulationManagerFactory.Xal_Id) {
            return;
        }
        if (!(manager.getChannelSource().equals(Scenario.SYNC_MODE_RF_DESIGN) || manager.getChannelSource().equals(Scenario.SYNC_MODE_MONITOR_ONLINE))) {
            return;
        }
        AcceleratorSeq seq = manager.getSequence();
        int count = 0;
        List<AcceleratorNode> list = seq.getAllNodes();
        count = list.size();
        if (count <= 0) {
            count = 1;
        }
        bpmxData = new double[1][count];
        bpmyData = new double[1][count];
        bpmPosData = new double[1][count];
        Iterator<AcceleratorNode> reciter = list.iterator();
        int icount = -1;
        while (reciter.hasNext()) {
            AcceleratorNode node = reciter.next();
            double z = seq.getPosition(node);
            if (node != null) {
                if (node.getType().equals("BPM")) {
                    BPM bpm = (BPM) node;
                    icount++;
                    bpmPosData[0][icount] = z;
                    try {
                        bpmxData[0][icount] = bpm.getXAvg();
                        bpmyData[0][icount] = bpm.getYAvg();
                    } catch (ConnectionException e) {
                        e.printStackTrace();
                    } catch (GetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        setViewVisible(false);
    }
