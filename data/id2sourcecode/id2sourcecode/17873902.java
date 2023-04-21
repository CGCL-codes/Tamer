    public static void convertFiles(OBOFileAdapter.OBOAdapterConfiguration readConfig, OBOFileAdapter.OBOAdapterConfiguration writeConfig, List<ScriptWrapper> scripts) throws Exception {
        OBOFileAdapter adapter = new OBOFileAdapter();
        OBOSession session = adapter.doOperation(OBOAdapter.READ_ONTOLOGY, readConfig, null);
        Iterator<ScriptWrapper> it = scripts.iterator();
        while (it.hasNext()) {
            ScriptWrapper wrapper = it.next();
            runScript(session, wrapper.getScript(), wrapper.getArgs());
        }
        logger.info("About to write files..., session object count = " + session.getObjects().size());
        logger.info("writePath = " + writeConfig.getWritePath());
        logger.info("savePath = " + writeConfig.getSaveRecords());
        SimpleLinkFileAdapter writer = new SimpleLinkFileAdapter();
        writer.doOperation(OBOAdapter.WRITE_ONTOLOGY, writeConfig, session);
    }
