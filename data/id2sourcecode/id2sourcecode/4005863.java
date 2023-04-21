    private void initReplicaLoop() throws IOException, ConnectRetryException, DatabaseException, ProtocolException, InterruptedException {
        createReplicaFeederChannel();
        ReplicaFeederHandshake handshake = new ReplicaFeederHandshake(repNode, replicaFeederChannel);
        protocol = handshake.execute();
        ReplicaFeederSyncup syncup = new ReplicaFeederSyncup(repNode, replay, replicaFeederChannel, protocol);
        syncup.execute(repNode.getCBVLSNTracker());
        VLSN matchedTxnVLSN = syncup.getMatchedVLSN();
        long matchedTxnCommitTime = syncup.getMatchedVLSNTime();
        consistencyTracker.reinit(matchedTxnVLSN.getSequence(), matchedTxnCommitTime);
        Protocol.Heartbeat heartbeat = protocol.read(replicaFeederChannel.getChannel(), Protocol.Heartbeat.class);
        processHeartbeat(replicaFeederChannel, heartbeat);
        long replicaDelta = consistencyTracker.getMasterCommitVLSN() - consistencyTracker.lastReplayedVLSN.getSequence();
        LoggerUtils.info(logger, repImpl, String.format("Replica initialization completed. Replica VLSN: %s " + " Heartbeat master commit VLSN: %,d " + "VLSN delta: %,d", consistencyTracker.lastReplayedVLSN, consistencyTracker.getMasterCommitVLSN(), replicaDelta));
        repNode.getReadyLatch().countDown();
    }
