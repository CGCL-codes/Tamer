    public static void convertFiles(OWLAdapter.OWLAdapterConfiguration readConfig, OBOFileAdapter.OBOAdapterConfiguration writeConfig, boolean parseObsoleteComments, boolean writeObsoleteComments, boolean fixDbxrefs, List<ScriptWrapper> scripts) throws Exception {
        OWLAdapter adapter = new OWLAdapter();
        OBOFileAdapter wadapter = new OBOFileAdapter();
        OBOSession session = adapter.doOperation(OWLAdapter.READ_ONTOLOGY, readConfig, null);
        Iterator<ScriptWrapper> it = scripts.iterator();
        while (it.hasNext()) {
            ScriptWrapper wrapper = it.next();
            runScript(session, wrapper.getScript(), wrapper.getArgs());
        }
        logger.info("About to write files..., session object count = " + session.getObjects().size());
        logger.info("writePath = " + writeConfig.getWritePath());
        logger.info("savePath = " + writeConfig.getSaveRecords());
        wadapter.doOperation(OBOAdapter.WRITE_ONTOLOGY, writeConfig, session);
    }
