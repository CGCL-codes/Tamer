    protected void fetchOrbit(final OrbitModel orbitModel, final long snapshotID) {
        MachineSnapshot machineSnapshot = _loggerStore.fetchMachineSnapshot(snapshotID);
        _loggerStore.loadChannelSnapshotsInto(machineSnapshot);
        Date timestamp = machineSnapshot.getTimestamp();
        ChannelSnapshot[] snapshots = machineSnapshot.getChannelSnapshots();
        Map snapshotMap = new HashMap();
        for (int index = 0; index < snapshots.length; index++) {
            ChannelSnapshot snapshot = snapshots[index];
            String signal = snapshot.getPV();
            snapshotMap.put(signal, snapshot);
        }
        MutableOrbit orbit = new MutableOrbit(orbitModel.getSequence());
        List<BpmAgent> bpmAgents = orbitModel.getBPMAgents();
        List signals = new ArrayList();
        for (BpmAgent bpmAgent : bpmAgents) {
            String xAvgPV = bpmAgent.getChannel(BPM.X_AVG_HANDLE).channelName();
            String yAvgPV = bpmAgent.getChannel(BPM.Y_AVG_HANDLE).channelName();
            String ampAvgPV = bpmAgent.getChannel(BPM.AMP_AVG_HANDLE).channelName();
            double xAvg = getValue(snapshotMap, xAvgPV);
            double yAvg = getValue(snapshotMap, yAvgPV);
            double ampAvg = getValue(snapshotMap, ampAvgPV);
            BpmRecord record = new BpmRecord(bpmAgent, timestamp, xAvg, yAvg, ampAvg);
            orbit.addRecord(record);
        }
        SnapshotOrbitSource orbitSource = (SnapshotOrbitSource) _orbitSource;
        orbitSource.setSnapshot(orbit.getOrbit());
    }
