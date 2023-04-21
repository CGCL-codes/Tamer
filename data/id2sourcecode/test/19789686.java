    public void checkRulesForExistingEnv(boolean dbTreeReplicatedBit) throws UnsupportedOperationException {
        if (dbTreeReplicatedBit && (!isReadOnly())) {
            throw new UnsupportedOperationException("This environment was previously opened for replication." + " It cannot be re-opened for in read/write mode for" + " non-replicated operation.");
        }
    }
