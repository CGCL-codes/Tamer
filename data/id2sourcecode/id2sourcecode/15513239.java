    public void setMachineSnapshot(MachineSnapshot snapshot) {
        _snapshot = snapshot;
        _channelSnapshots = (snapshot != null) ? snapshot.getChannelSnapshots() : new ChannelSnapshot[0];
        _filteredChannelSnapshots = _controller.filterSnapshots(_channelSnapshots);
        fireTableDataChanged();
    }
