    public synchronized void store(LogConnection connection) throws ReplicatorException {
        assertNotDone(connection);
        if (writeConnection != null && writeConnection.isDone()) writeConnection = null;
        int readConnectionsSize = readConnections.size();
        for (int i = 0; i < readConnectionsSize; i++) {
            int index = readConnectionsSize - i - 1;
            if (readConnections.get(index).isDone()) readConnections.remove(index);
        }
        if (!connection.isReadonly() && writeConnection != null) throw new THLException("Write connection already exists: connection=" + writeConnection.toString());
        if (connection.isReadonly()) readConnections.add(connection); else writeConnection = connection;
    }
