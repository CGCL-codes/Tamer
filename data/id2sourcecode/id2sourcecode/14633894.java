    protected void logConfiguration() {
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug("================= Persistence Configuration ===============");
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("008000", "DataNucleus", pluginManager.getVersionForBundle("org.datanucleus")));
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("008001", config.getStringProperty(PropertyNames.PROPERTY_CONNECTION_URL), config.getStringProperty(PropertyNames.PROPERTY_CONNECTION_DRIVER_NAME), config.getStringProperty(PropertyNames.PROPERTY_CONNECTION_USER_NAME)));
            NucleusLogger.PERSISTENCE.debug("JDK : " + System.getProperty("java.version") + " on " + System.getProperty("os.name"));
            NucleusLogger.PERSISTENCE.debug("Persistence API : " + apiAdapter.getName());
            NucleusLogger.PERSISTENCE.debug("Plugin Registry : " + pluginManager.getRegistryClassName());
            if (config.hasPropertyNotNull(PropertyNames.PROPERTY_PERSISTENCE_UNIT_NAME)) {
                NucleusLogger.PERSISTENCE.debug("Persistence-Unit : " + config.getStringProperty(PropertyNames.PROPERTY_PERSISTENCE_UNIT_NAME));
            }
            String timeZoneID = config.getStringProperty(PropertyNames.PROPERTY_SERVER_TIMEZONE_ID);
            if (timeZoneID == null) {
                timeZoneID = TimeZone.getDefault().getID();
            }
            NucleusLogger.PERSISTENCE.debug("Standard Options : " + (config.getBooleanProperty(PropertyNames.PROPERTY_MULTITHREADED) ? "pm-multithreaded" : "pm-singlethreaded") + (config.getBooleanProperty(PropertyNames.PROPERTY_RETAIN_VALUES) ? ", retain-values" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_RESTORE_VALUES) ? ", restore-values" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_NONTX_READ) ? ", nontransactional-read" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_NONTX_WRITE) ? ", nontransactional-write" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_IGNORE_CACHE) ? ", ignoreCache" : "") + ", serverTimeZone=" + timeZoneID);
            NucleusLogger.PERSISTENCE.debug("Persistence Options :" + (config.getBooleanProperty(PropertyNames.PROPERTY_PERSISTENCE_BY_REACHABILITY_AT_COMMIT) ? " reachability-at-commit" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_DETACH_ALL_ON_COMMIT) ? " detach-all-on-commit" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_DETACH_ALL_ON_ROLLBACK) ? " detach-all-on-rollback" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_DETACH_ON_CLOSE) ? " detach-on-close" : "") + (config.getBooleanProperty(PropertyNames.PROPERTY_MANAGE_RELATIONSHIPS) ? (config.getBooleanProperty(PropertyNames.PROPERTY_MANAGE_RELATIONSHIPS_CHECKS) ? " managed-relations(checked)" : "managed-relations(unchecked)") : "") + " deletion-policy=" + config.getStringProperty(PropertyNames.PROPERTY_DELETION_POLICY));
            NucleusLogger.PERSISTENCE.debug("Transactions : type=" + config.getStringProperty(PropertyNames.PROPERTY_TRANSACTION_TYPE) + " mode=" + (config.getBooleanProperty(PropertyNames.PROPERTY_OPTIMISTIC) ? "optimistic" : "datastore") + " isolation=" + config.getStringProperty(PropertyNames.PROPERTY_TRANSACTION_ISOLATION));
            NucleusLogger.PERSISTENCE.debug("Value Generation :" + " txn-isolation=" + config.getStringProperty(PropertyNames.PROPERTY_VALUEGEN_TXN_ISOLATION) + " connection=" + (config.getStringProperty(PropertyNames.PROPERTY_VALUEGEN_TXN_ATTRIBUTE).equalsIgnoreCase("New") ? "New" : "Existing"));
            Object primCL = config.getProperty(PropertyNames.PROPERTY_CLASSLOADER_PRIMARY);
            NucleusLogger.PERSISTENCE.debug("ClassLoading : " + config.getStringProperty(PropertyNames.PROPERTY_CLASSLOADER_RESOLVER_NAME) + (primCL != null ? ("primary=" + primCL) : ""));
            NucleusLogger.PERSISTENCE.debug("Cache : Level1 (" + config.getStringProperty(PropertyNames.PROPERTY_CACHE_L1_TYPE) + ")" + ", Level2 (" + config.getStringProperty(PropertyNames.PROPERTY_CACHE_L2_TYPE) + ", mode=" + config.getStringProperty(PropertyNames.PROPERTY_CACHE_L2_MODE) + ")" + ", QueryResults (" + config.getStringProperty(PropertyNames.PROPERTY_CACHE_QUERYRESULTS_TYPE) + ")" + (config.getBooleanProperty(PropertyNames.PROPERTY_CACHE_COLLECTIONS) ? ", Collections/Maps " : ""));
            NucleusLogger.PERSISTENCE.debug("===========================================================");
        }
    }
