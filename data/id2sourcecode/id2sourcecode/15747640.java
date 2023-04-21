    public void testParser() throws Exception {
        String filename = getClass().getResource("/" + FileChangeLog.RECENT_FILE).getFile();
        File file = new File(filename);
        FileUtils.copyFileToDirectory(file, wikiDir);
        assertTrue(Utilities.isJsxFile(file));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 13);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.YEAR, 2008);
        Collection changes = fileChangeLog.getChanges(WikiBase.DEFAULT_VWIKI, calendar.getTime());
        logger.info("loaded changes: " + changes);
    }
