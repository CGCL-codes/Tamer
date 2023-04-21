    public static void convertFiles(OBOFileAdapter.OBOAdapterConfiguration readConfig, OWLAdapter.OWLAdapterConfiguration writeConfig, boolean parseObsoleteComments, boolean writeObsoleteComments, boolean fixDbxrefs, List scripts) throws Exception {
        OBOFileAdapter adapter = new OBOFileAdapter();
        OBOSession session = adapter.doOperation(OBOAdapter.READ_ONTOLOGY, readConfig, null);
        if (parseObsoleteComments) {
            parseComments(session);
        }
        if (writeObsoleteComments) {
            writeComments(session);
        }
        Iterator it = scripts.iterator();
        while (it.hasNext()) {
            ScriptWrapper wrapper = (ScriptWrapper) it.next();
            runScript(session, wrapper.getScript(), wrapper.getArgs());
        }
        logger.info("About to write files..., session object count = " + session.getObjects().size());
        logger.info("writePath = " + writeConfig.getWritePath());
        logger.info("savePath = " + writeConfig.getSaveRecords());
        OWLAdapter wadapter = new OWLAdapter();
        wadapter.doOperation(OWLAdapter.WRITE_ONTOLOGY, writeConfig, session);
    }
