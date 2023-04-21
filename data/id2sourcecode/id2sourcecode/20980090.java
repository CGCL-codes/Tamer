    @Override
    public MachineSnapshotSP takeSnapShotNoBuffer() {
        final ChannelWrapper[] channelWrappers = ((ChannelGroupSP) _group).getChannelWrappers();
        final ChannelWrapper[] channelWrappersSP = ((ChannelGroupSP) _group).getChannelWrappers();
        MachineSnapshotSP machineSnapshot = new MachineSnapshotSP(channelWrappers.length);
        machineSnapshot.setType(_group.getLabel());
        for (int index = 0; index < channelWrappers.length; index++) {
            ChannelWrapper channelWrapper = channelWrappers[index];
            if (channelWrapper == null) continue;
            ChannelWrapper channelWrapperSP = channelWrappersSP[index];
            ChannelTimeRecord record = channelWrapper.getRecord();
            if (record == null) continue;
            ChannelSnapshot snapshot = new ChannelSnapshot(channelWrapper.getPV(), record);
            ChannelTimeRecord recordSP = channelWrapperSP.getRecord();
            ChannelSnapshot snapshotSP = new ChannelSnapshot(channelWrapperSP.getPV(), recordSP);
            machineSnapshot.setChannelSnapshot(index, snapshot);
            machineSnapshot.setChannelSnapshotSP(index, snapshotSP);
        }
        changeProxy.snapshotTaken(this, machineSnapshot);
        System.out.println("current time (ms) = " + System.currentTimeMillis());
        return machineSnapshot;
    }
