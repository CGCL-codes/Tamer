    @Test(dependsOnMethods = "testFolderIndexing")
    public void testFolderChange() throws IOException {
        CurrentFolderIndexer indexer = new CurrentFolderIndexer(null, cmdHandler);
        indexer.updateFolder(topFolder);
        BackgroundTask task = indexer.requestTask();
        assertTrue(task instanceof DirTreeIndexerTask);
        PhotoFolder subfolder = topFolder.getSubfolders().iterator().next();
        indexer.updateFolder(subfolder);
        task = indexer.requestTask();
        assertTrue(task instanceof DirTreeIndexerTask);
        File subsubdir = new File(topDir, "subdir/subsubdir");
        subsubdir.mkdir();
        task.executeTask(session, cmdHandler);
        assertEquals(1, subfolder.getSubfolders().size());
        File t2 = new File(testfileDir, "test2.jpg");
        File tt2 = new File(topDir, "subdir/test2.jpg");
        FileUtils.copyFile(t2, tt2);
        task = indexer.requestTask();
        while (!(task instanceof IndexFileTask)) {
            task.executeTask(session, cmdHandler);
            task = indexer.requestTask();
        }
        assertEquals(tt2, ((IndexFileTask) task).getFile());
    }
