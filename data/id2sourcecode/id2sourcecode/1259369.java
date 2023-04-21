    private void createUpgradeHistoryTable() throws FacilitiesException {
        ApplicationFacilitiesLocal af = null;
        PersistenceFacilitiesLocal pf = null;
        long newTableID = 0L;
        try {
            logger.info("Preparing to create " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table.");
            writeLog("Looking up ApplicationFacilities.");
            af = applicationFacilitiesBean;
            writeLog("Looking up PersistenceFacilities.");
            pf = persistenceFacilitiesBean;
            writeLog("Fetching " + DatabaseConstants.TableName_JV_TABLEATTRS + " TableAgent instance.");
            TableAgent tableAttrsTA = getTableAgent(DatabaseConstants.TableName_JV_TABLEATTRS);
            ObjectAttributeSet tableAttrsOAS = tableAttrsTA.getTableAgentObjectAttributes();
            writeLog("Determining whether defunct table attributes already exist for table " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + ".");
            ObjectAttributeSet existingUpgradeHistoryTableAttrsRecordOAS = tableAttrsTA.lookupRecord(DatabaseConstants.TableFieldName_JV_TABLEATTRS_NAME, DatabaseConstants.TableName_JV_UPGRADEHISTORY, true);
            if (existingUpgradeHistoryTableAttrsRecordOAS != null) {
                writeLog("Defunct attributes for table " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " were found.  Purging.");
                tableAttrsTA.deleteRecordWithoutRegardForDependencies(existingUpgradeHistoryTableAttrsRecordOAS.getPrimaryKeyValue());
                writeLog("Defunct attributes for table " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " were removed successfully.");
            }
            writeLog("Determing " + Constants.sfJVantageDeveloperApplicationName + " application ID.");
            long jVantageApplicationID = af.getApplicationID(Constants.sfJVantageDeveloperApplicationName);
            writeLog(Constants.sfJVantageDeveloperApplicationName + " application ID: " + jVantageApplicationID);
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_NAME, DatabaseConstants.TableName_JV_UPGRADEHISTORY);
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_ENTITYNAME, "UpgradeHistory");
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_SHORTDESC, "Keeps a record of all upgrades to this jVantage instance.");
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_DESCRIPTION, "Keeps a record of all upgrades to this jVantage instance.");
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_DESCRIPTIVENAMEFIELD, DatabaseConstants.TableFieldName_JV_UPGRADEHISTORY_NAME);
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_ELEMENTNAME, "Upgrade Event");
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_PLURALOFELEMENTNAME, "Upgrade History");
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_SECUREWITHSSL, Constants.sfNo);
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_SINGLETON, Constants.sfNo);
            setUpgradeHistoryAttributeValue(tableAttrsOAS, DatabaseConstants.TableFieldName_JV_TABLEATTRS_APPLICATION, new Long(jVantageApplicationID));
            writeLog("Inserting table attributes.");
            newTableID = tableAttrsTA.insertDatabaseRecord(tableAttrsOAS);
            writeLog(DatabaseConstants.TableName_JV_UPGRADEHISTORY + " tableID is [" + newTableID + "].");
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new FacilitiesException("Exception", e);
        }
        try {
            writeLog("Preparing to create " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table in underyling database.");
            pf.createNewlyDefinedTable(newTableID);
            writeLog(DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table created successfully.");
            writeLog("Creating name field in " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table.");
            pf.insertNameFieldIntoTableDefinition(DatabaseConstants.TableName_JV_UPGRADEHISTORY);
            writeLog("Creating description field in " + DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table.");
            pf.insertDescriptionFieldIntoTableDefinition(DatabaseConstants.TableName_JV_UPGRADEHISTORY);
            writeLog(DatabaseConstants.TableName_JV_UPGRADEHISTORY + " table created successfully.");
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new FacilitiesException("Exception", e);
        }
    }
