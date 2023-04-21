    @Override
    public synchronized MachineSnapshot takeSnapShotBuffer() {
        final ChannelWrapper[] channelWrappers = ((ChannelGroupSP) _group).getChannelWrappers();
        final ChannelWrapper[] channelWrappersSP = ((ChannelGroupSP) _group).getChannelWrappersSP();
        ArrayList<ChannelSnapshot> snapshots = new ArrayList<ChannelSnapshot>();
        ArrayList<ChannelSnapshot> snapshotsSP = new ArrayList<ChannelSnapshot>();
        int ichannel = 0;
        for (int index = 0; index < channelWrappers.length; index++) {
            ChannelBufferWrapper channelWrapper = (ChannelBufferWrapper) channelWrappers[index];
            ChannelBufferWrapper channelWrapperSP = (ChannelBufferWrapper) channelWrappersSP[index];
            List<ChannelTimeRecord> records = channelWrapper.getRecords();
            List<ChannelTimeRecord> recordsSP = channelWrapperSP.getRecords();
            for (int j = 0; j < records.size(); j++) {
                ChannelTimeRecord record = records.get(j);
                ChannelTimeRecord recordSP = null;
                if (j < recordsSP.size()) {
                    recordSP = recordsSP.get(j);
                }
                if (record == null) continue;
                ChannelSnapshot snapshot = new ChannelSnapshot(channelWrapper.getPV(), record);
                ChannelSnapshot snapshotSP = null;
                if (recordsSP != null) {
                    snapshotSP = new ChannelSnapshot(channelWrapperSP.getPV(), recordSP);
                }
                if (records.size() > 1) {
                    System.out.println("update " + ichannel + " : " + snapshot.getPV() + " value = " + snapshot.getValue()[0] + " time = " + snapshot.getTimestamp());
                }
                snapshots.add(snapshot);
                snapshotsSP.add(snapshotSP);
                ichannel++;
            }
            channelWrapper.clear();
            channelWrapperSP.clear();
        }
        System.out.println("LoggerSession::takeSnapshotBuffer number of update records = " + ichannel);
        if (snapshots.size() == 0) {
            return null;
        }
        ChannelSnapshot[] channelSnapshots = snapshots.toArray(new ChannelSnapshot[snapshots.size()]);
        ChannelSnapshot[] channelSnapshotsSP = snapshotsSP.toArray(new ChannelSnapshot[snapshotsSP.size()]);
        MachineSnapshotSP machineSnapshot = new MachineSnapshotSP(new Date(), "", channelSnapshots, channelSnapshotsSP);
        machineSnapshot.setType(_group.getLabel());
        changeProxy.snapshotTaken(this, machineSnapshot);
        System.out.println("current time (ms) = " + System.currentTimeMillis());
        return machineSnapshot;
    }
