    public void publish(final ChannelSnapshot[] snapshots, final long machineId) throws StateStoreException {
        String pvs[] = new String[snapshots.length];
        Timestamp times[] = new Timestamp[snapshots.length];
        double values[][] = new double[snapshots.length][];
        int stats[] = new int[snapshots.length];
        int severs[] = new int[snapshots.length];
        int count = 0;
        for (int index = 0; index < snapshots.length; index++) {
            ChannelSnapshot snapshot = snapshots[index];
            if (snapshot != null) {
                pvs[count] = snapshot.getPV();
                times[count] = snapshot.getTimestamp().getSQLTimestamp();
                values[count] = snapshot.getValue();
                stats[count] = snapshot.getStatus();
                severs[count] = snapshot.getSeverity();
                count++;
            }
        }
        if ((count > 0) && (count < snapshots.length)) {
            String pvsnew[] = new String[count];
            System.arraycopy(pvs, 0, pvsnew, 0, count);
            pvs = pvsnew;
            Timestamp timesnew[] = new Timestamp[count];
            System.arraycopy(times, 0, timesnew, 0, count);
            times = timesnew;
            double valuesnew[][] = new double[count][];
            System.arraycopy(values, 0, valuesnew, 0, count);
            values = valuesnew;
            int statsnew[] = new int[count];
            System.arraycopy(stats, 0, statsnew, 0, count);
            stats = statsnew;
            int seversnew[] = new int[count];
            System.arraycopy(severs, 0, seversnew, 0, count);
            severs = seversnew;
        }
        if (count > 0) {
            try {
                getChannelSnapshotInsertStatement();
                CHANNEL_SNAPSHOT_INSERT.setLong(1, machineId);
                Array pvArray = _databaseAdaptor.getArray("float4", _connection, pvs);
                CHANNEL_SNAPSHOT_INSERT.setArray(2, pvArray);
                Array timestampArray = _databaseAdaptor.getArray("timestamp", _connection, times);
                CHANNEL_SNAPSHOT_INSERT.setArray(3, timestampArray);
                Array valueArray = _databaseAdaptor.getArray("float4", _connection, values);
                CHANNEL_SNAPSHOT_INSERT.setArray(4, valueArray);
                Array statArray = _databaseAdaptor.getArray("int4", _connection, stats);
                CHANNEL_SNAPSHOT_INSERT.setArray(5, statArray);
                Array severArray = _databaseAdaptor.getArray("int4", _connection, severs);
                CHANNEL_SNAPSHOT_INSERT.setArray(6, severArray);
                CHANNEL_SNAPSHOT_INSERT.addBatch();
            } catch (SQLException exception) {
                throw new StateStoreException("Error publishing a channel snapshot.", exception);
            }
        } else {
            System.out.println("count = " + count);
        }
    }
