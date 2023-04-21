    protected void logConfiguration() {
        if (NucleusLogger.DATASTORE.isDebugEnabled()) {
            NucleusLogger.DATASTORE.debug("======================= Datastore =========================");
            NucleusLogger.DATASTORE.debug("StoreManager : \"" + storeManagerKey + "\" (" + getClass().getName() + ")");
            if (autoStartMechanism != null) {
                String classNames = getStringProperty(PropertyNames.PROPERTY_AUTOSTART_CLASSNAMES);
                NucleusLogger.DATASTORE.debug("AutoStart : mechanism=" + autoStartMechanism + ", mode=" + getStringProperty(PropertyNames.PROPERTY_AUTOSTART_MODE) + ((classNames != null) ? (", classes=" + classNames) : ""));
            }
            NucleusLogger.DATASTORE.debug("Datastore : " + (readOnlyDatastore ? "read-only" : "read-write") + (fixedDatastore ? ", fixed" : "") + (getBooleanProperty(PropertyNames.PROPERTY_SERIALIZE_READ) ? ", useLocking" : ""));
            StringBuffer autoCreateOptions = null;
            if (autoCreateTables || autoCreateColumns || autoCreateConstraints) {
                autoCreateOptions = new StringBuffer();
                boolean first = true;
                if (autoCreateTables) {
                    if (!first) {
                        autoCreateOptions.append(",");
                    }
                    autoCreateOptions.append("Tables");
                    first = false;
                }
                if (autoCreateColumns) {
                    if (!first) {
                        autoCreateOptions.append(",");
                    }
                    autoCreateOptions.append("Columns");
                    first = false;
                }
                if (autoCreateConstraints) {
                    if (!first) {
                        autoCreateOptions.append(",");
                    }
                    autoCreateOptions.append("Constraints");
                    first = false;
                }
            }
            StringBuffer validateOptions = null;
            if (validateTables || validateColumns || validateConstraints) {
                validateOptions = new StringBuffer();
                boolean first = true;
                if (validateTables) {
                    validateOptions.append("Tables");
                    first = false;
                }
                if (validateColumns) {
                    if (!first) {
                        validateOptions.append(",");
                    }
                    validateOptions.append("Columns");
                    first = false;
                }
                if (validateConstraints) {
                    if (!first) {
                        validateOptions.append(",");
                    }
                    validateOptions.append("Constraints");
                    first = false;
                }
            }
            NucleusLogger.DATASTORE.debug("Schema Control : " + "AutoCreate(" + (autoCreateOptions != null ? autoCreateOptions.toString() : "None") + ")" + ", Validate(" + (validateOptions != null ? validateOptions.toString() : "None") + ")");
            String[] queryLanguages = nucleusContext.getPluginManager().getAttributeValuesForExtension("org.datanucleus.store_query_query", "datastore", storeManagerKey, "name");
            NucleusLogger.DATASTORE.debug("Query Languages : " + (queryLanguages != null ? StringUtils.objectArrayToString(queryLanguages) : "none"));
            NucleusLogger.DATASTORE.debug("Queries : Timeout=" + getIntProperty(PropertyNames.PROPERTY_DATASTORE_READ_TIMEOUT));
            NucleusLogger.DATASTORE.debug("===========================================================");
        }
    }
