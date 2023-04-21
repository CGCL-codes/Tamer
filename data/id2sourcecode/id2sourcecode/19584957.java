    public void testTpbMapper() throws Exception {
        FBTpbMapper mapper = new FBTpbMapper(mcf.getGDS(), TEST_TPB_MAPPING, getClass().getClassLoader());
        mcf.setTpbMapping(TEST_TPB_MAPPING);
        mcf.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        TransactionParameterBuffer tpbValue = mcf.getDefaultTpb().getTransactionParameterBuffer();
        assertTrue("READ_COMMITED must be isc_tpb_read_committed+" + "isc_tpb_no_rec_version+isc_tpb_write+isc_tpb_nowait", tpbValue.hasArgument(ISCConstants.isc_tpb_read_committed) && tpbValue.hasArgument(ISCConstants.isc_tpb_no_rec_version) && tpbValue.hasArgument(ISCConstants.isc_tpb_write) && tpbValue.hasArgument(ISCConstants.isc_tpb_nowait));
        mcf.setDefaultTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        tpbValue = mcf.getDefaultTpb().getTransactionParameterBuffer();
        assertTrue("REPEATABLE_READ must be isc_tpb_consistency+" + "isc_tpb_write+isc_tpb_wait", tpbValue.hasArgument(ISCConstants.isc_tpb_consistency) && tpbValue.hasArgument(ISCConstants.isc_tpb_write) && tpbValue.hasArgument(ISCConstants.isc_tpb_wait));
        mcf.setDefaultTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        tpbValue = mcf.getDefaultTpb().getTransactionParameterBuffer();
        assertTrue("SERIALIZABLE must be isc_tpb_concurrency+" + "isc_tpb_write+isc_tpb_wait", tpbValue.hasArgument(ISCConstants.isc_tpb_concurrency) && tpbValue.hasArgument(ISCConstants.isc_tpb_write) && tpbValue.hasArgument(ISCConstants.isc_tpb_wait));
    }
