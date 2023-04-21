    protected void logConfiguration() {
        NucleusLogger.PERSISTENCE.info("================= Persistence Configuration ===============");
        NucleusLogger.PERSISTENCE.info(LOCALISER.msg("008000", getVendorName(), getVersionNumber()));
        NucleusLogger.PERSISTENCE.info(LOCALISER.msg("008001", getStringProperty("datanucleus.ConnectionURL"), getStringProperty("datanucleus.ConnectionDriverName"), getStringProperty("datanucleus.ConnectionUserName")));
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug("JDK : " + System.getProperty("java.version") + " on " + System.getProperty("os.name"));
            NucleusLogger.PERSISTENCE.debug("Persistence API : " + getOMFContext().getApi());
            NucleusLogger.PERSISTENCE.debug("Plugin Registry : " + getOMFContext().getPluginManager().getRegistryClassName());
            if (hasProperty("datanucleus.PersistenceUnitName")) {
                NucleusLogger.PERSISTENCE.debug("Persistence-Unit : " + getStringProperty("datanucleus.PersistenceUnitName"));
            }
            String timeZoneID = getStringProperty("datanucleus.ServerTimeZoneID");
            if (timeZoneID == null) {
                timeZoneID = TimeZone.getDefault().getID();
            }
            NucleusLogger.PERSISTENCE.debug("Standard Options : " + (getBooleanProperty("datanucleus.Multithreaded") ? "multithreaded" : "singlethreaded") + (getBooleanProperty("datanucleus.RetainValues") ? ", retain-values" : "") + (getBooleanProperty("datanucleus.RestoreValues") ? ", restore-values" : "") + (getBooleanProperty("datanucleus.NontransactionalRead") ? ", nontransactional-read" : "") + (getBooleanProperty("datanucleus.NontransactionalWrite") ? ", nontransactional-write" : "") + (getBooleanProperty("datanucleus.IgnoreCache") ? ", ignoreCache" : "") + ", serverTimeZone=" + timeZoneID);
            NucleusLogger.PERSISTENCE.debug("Persistence Options :" + (getBooleanProperty("datanucleus.persistenceByReachabilityAtCommit") ? " reachability-at-commit" : "") + (getBooleanProperty("datanucleus.DetachAllOnCommit") ? " detach-all-on-commit" : "") + (getBooleanProperty("datanucleus.DetachAllOnRollback") ? " detach-all-on-rollback" : "") + (getBooleanProperty("datanucleus.DetachOnClose") ? " detach-on-close" : "") + (getBooleanProperty("datanucleus.manageRelationships") ? (getBooleanProperty("datanucleus.manageRelationshipsChecks") ? " managed-relations(checked)" : "managed-relations(unchecked)") : "") + " deletion-policy=" + getStringProperty("datanucleus.deletionPolicy"));
            NucleusLogger.PERSISTENCE.debug("Transactions : type=" + getStringProperty("datanucleus.TransactionType") + " mode=" + (getBooleanProperty("datanucleus.Optimistic") ? "optimistic" : "datastore") + " isolation=" + getStringProperty("datanucleus.transactionIsolation"));
            NucleusLogger.PERSISTENCE.debug("Value Generation :" + " txn-isolation=" + getStringProperty("datanucleus.valuegeneration.transactionIsolation") + " connection=" + (getStringProperty("datanucleus.valuegeneration.transactionAttribute").equalsIgnoreCase("New") ? "New" : "PM"));
            Object primCL = getProperty("datanucleus.primaryClassLoader");
            NucleusLogger.PERSISTENCE.debug("ClassLoading : " + getStringProperty("datanucleus.classLoaderResolverName") + (primCL != null ? ("primary=" + primCL) : ""));
            NucleusLogger.PERSISTENCE.debug("Cache : Level1 (" + getStringProperty("datanucleus.cache.level1.type") + ")" + ", Level2 (" + getStringProperty("datanucleus.cache.level2.type") + ")" + ", QueryResults (" + getStringProperty("datanucleus.cache.queryResults.type") + ")" + (getBooleanProperty("datanucleus.cache.collections") ? ", Collections/Maps " : ""));
        }
        NucleusLogger.PERSISTENCE.info("===========================================================");
    }
