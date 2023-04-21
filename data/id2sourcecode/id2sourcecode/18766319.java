    @Test
    public void testFolderIndexing() throws IOException {
        File subdir = new File(topDir, "subdir");
        subdir.mkdir();
        File testfile = new File(testfileDir, "test1.jpg");
        FileUtils.copyFile(testfile, new File(topDir, "test1.jpg"));
        CurrentFolderIndexer indexer = new CurrentFolderIndexer(null, cmdHandler);
        indexer.updateFolder(topFolder);
        BackgroundTask task = indexer.requestTask();
        assertTrue(task instanceof DirTreeIndexerTask);
        task.executeTask(session, cmdHandler);
        task = indexer.requestTask();
        task.executeTask(session, cmdHandler);
        task = indexer.requestTask();
        assertTrue(task instanceof IndexFileTask);
        task.executeTask(session, cmdHandler);
        task = indexer.requestTask();
        task.executeTask(session, cmdHandler);
        task = indexer.requestTask();
        assertNull(task);
    }
