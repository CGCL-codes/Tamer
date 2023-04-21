    public synchronized MachineSnapshot takeSnapShotBuffer() {
        final ChannelWrapper[] channelWrappers = _group.getChannelWrappers();
        ArrayList<ChannelSnapshot> snapshots = new ArrayList<ChannelSnapshot>();
        int ichannel = 0;
        for (int index = 0; index < channelWrappers.length; index++) {
            ChannelBufferWrapper channelWrapper = (ChannelBufferWrapper) channelWrappers[index];
            List<ChannelTimeRecord> records = channelWrapper.getRecords();
            for (int j = 0; j < records.size(); j++) {
                ChannelTimeRecord record = records.get(j);
                if (record == null) continue;
                ChannelSnapshot snapshot = new ChannelSnapshot(channelWrapper.getPV(), record);
                if (records.size() > 1) {
                    System.out.println("update " + ichannel + " : " + snapshot.getPV() + " value = " + snapshot.getValue()[0] + " time = " + snapshot.getTimestamp());
                }
                snapshots.add(snapshot);
                ichannel++;
            }
            channelWrapper.clear();
        }
        System.out.println("LoggerSession::takeSnapshotBuffer number of update records = " + ichannel);
        if (snapshots.size() == 0) {
            return null;
        }
        ChannelSnapshot[] channelSnapshots = snapshots.toArray(new ChannelSnapshot[snapshots.size()]);
        MachineSnapshot machineSnapshot = new MachineSnapshot(new Date(), "", channelSnapshots);
        machineSnapshot.setType(_group.getLabel());
        changeProxy.snapshotTaken(this, machineSnapshot);
        System.out.println("current time (ms) = " + System.currentTimeMillis());
        return machineSnapshot;
    }
