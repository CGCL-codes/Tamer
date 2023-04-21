    public static void convertFiles(OBDSQLDatabaseAdapter.OBDSQLDatabaseAdapterConfiguration readConfig, OBOFileAdapter.OBOAdapterConfiguration writeConfig, List<ScriptWrapper> scripts, OBOAdapter writer) throws Exception {
        OBDSQLDatabaseAdapter adapter = new OBDSQLDatabaseAdapter();
        OBOSession session = adapter.doOperation(OBOAdapter.READ_ONTOLOGY, readConfig, null);
        Iterator<ScriptWrapper> it = scripts.iterator();
        while (it.hasNext()) {
            ScriptWrapper wrapper = it.next();
            runScript(session, wrapper.getScript(), wrapper.getArgs());
        }
        logger.info("About to write files..., session object count = " + session.getObjects().size());
        logger.info("writePath = " + writeConfig.getWritePath());
        logger.info("savePath = " + writeConfig.getSaveRecords());
        writer.doOperation(OBOAdapter.WRITE_ONTOLOGY, writeConfig, session);
    }
