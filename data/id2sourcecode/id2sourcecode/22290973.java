    public MachineSnapshot takeSnapShotNoBuffer() {
        final ChannelWrapper[] channelWrappers = _group.getChannelWrappers();
        MachineSnapshot machineSnapshot = new MachineSnapshot(channelWrappers.length);
        machineSnapshot.setType(_group.getLabel());
        for (int index = 0; index < channelWrappers.length; index++) {
            ChannelWrapper channelWrapper = channelWrappers[index];
            if (channelWrapper == null) continue;
            ChannelTimeRecord record = channelWrapper.getRecord();
            if (record == null) continue;
            ChannelSnapshot snapshot = new ChannelSnapshot(channelWrapper.getPV(), record);
            machineSnapshot.setChannelSnapshot(index, snapshot);
        }
        changeProxy.snapshotTaken(this, machineSnapshot);
        System.out.println("current time (ms) = " + System.currentTimeMillis());
        return machineSnapshot;
    }
