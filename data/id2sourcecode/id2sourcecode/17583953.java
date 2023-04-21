    public MachineSnapshot populateSnapshot(final MachineSnapshot snapshot) {
        return (snapshot.getChannelCount() == 0) ? _loggerStore.loadChannelSnapshotsInto(snapshot) : snapshot;
    }
