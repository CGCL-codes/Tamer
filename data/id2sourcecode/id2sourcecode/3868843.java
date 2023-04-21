    private void checkFeeder(Protocol protocol) throws IOException, DatabaseException {
        protocol.read(namedChannel.getChannel(), FeederInfoReq.class);
        int feeders = feederManager.getActiveFeederCount() - 1;
        VLSN rangeFirst = VLSN.NULL_VLSN;
        VLSN rangeLast = VLSN.NULL_VLSN;
        if (feederManager.getEnvImpl() instanceof RepImpl) {
            RepImpl repImpl = (RepImpl) feederManager.getEnvImpl();
            feeders += repImpl.getRepNode().feederManager().activeReplicaCount();
            VLSNRange range = repImpl.getVLSNIndex().getRange();
            rangeFirst = range.getFirst();
            rangeLast = range.getLast();
        }
        protocol.write(protocol.new FeederInfoResp(feeders, rangeFirst, rangeLast), namedChannel);
    }
