    public void testCopyScriptFile() throws Exception {
        DataSource dump = new HypersonicDataSource(path, true);
        BackupUtils.importDataSource(DataSource.getInstance(), dump);
        dump.release();
        FileUtils.copyFile(path + ".script", path + "-copy.script", true);
        DataSource copy = new HypersonicDataSource(path + "-copy", false);
        Document doc = copy.loadDocument("dumpDoc");
        assertEquals("dumpContent", doc.getContentAsString(new Locale("en")));
    }
