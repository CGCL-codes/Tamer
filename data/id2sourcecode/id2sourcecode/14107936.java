    public FBTpbMapper(GDS gds) {
        TransactionParameterBuffer serializableTpb = gds.newTransactionParameterBuffer();
        serializableTpb.addArgument(ISCConstants.isc_tpb_write);
        serializableTpb.addArgument(ISCConstants.isc_tpb_wait);
        serializableTpb.addArgument(ISCConstants.isc_tpb_consistency);
        TransactionParameterBuffer repeatableReadTpb = gds.newTransactionParameterBuffer();
        repeatableReadTpb.addArgument(ISCConstants.isc_tpb_write);
        repeatableReadTpb.addArgument(ISCConstants.isc_tpb_wait);
        repeatableReadTpb.addArgument(ISCConstants.isc_tpb_concurrency);
        TransactionParameterBuffer readCommittedTpb = gds.newTransactionParameterBuffer();
        readCommittedTpb.addArgument(ISCConstants.isc_tpb_write);
        readCommittedTpb.addArgument(ISCConstants.isc_tpb_wait);
        readCommittedTpb.addArgument(ISCConstants.isc_tpb_read_committed);
        readCommittedTpb.addArgument(ISCConstants.isc_tpb_rec_version);
        mapping.put(new Integer(Connection.TRANSACTION_SERIALIZABLE), serializableTpb);
        mapping.put(new Integer(Connection.TRANSACTION_REPEATABLE_READ), repeatableReadTpb);
        mapping.put(new Integer(Connection.TRANSACTION_READ_COMMITTED), readCommittedTpb);
    }
