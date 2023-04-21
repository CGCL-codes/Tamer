    public void testIndexing() {
        int n;
        ExternalVolume v = new ExternalVolume("extVol", extVolDir.getAbsolutePath());
        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        PVDatabase db = settings.getDatabase("pv_junit");
        try {
            db.addVolume(v);
        } catch (PhotovaultException ex) {
            fail(ex.getMessage());
        }
        ExtVolIndexer indexer = new ExtVolIndexer(v);
        indexer.setTopFolder(topFolder);
        TestListener l = new TestListener();
        indexer.addIndexerListener(l);
        assertEquals("Indexing not started -> completeness must be 0", 0, indexer.getPercentComplete());
        assertNull("StartTime must be null before starting", indexer.getStartTime());
        indexer.run();
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        PhotoInfo[] photos1 = PhotoInfo.retrieveByOrigHash(hash1);
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        assertNotNull("photos1 = null", photos1);
        assertEquals("Only 1 photo per picture should be found", 1, photos1.length);
        PhotoInfo p1 = photos1[0];
        assertEquals("2 instances should be found in photo 1", 2, p1.getNumInstances());
        PhotoInfo[] photos2 = PhotoInfo.retrieveByOrigHash(hash2);
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        assertEquals("1 photo per picture should be found", 1, photos2.length);
        PhotoInfo p2 = photos2[0];
        assertEquals("3 instances should be found in photo 2", 3, p2.getNumInstances());
        boolean found[] = { false, false };
        File files[] = { photo2inst1, photo2inst2 };
        for (n = 0; n < p2.getNumInstances(); n++) {
            ImageInstance i = p2.getInstance(n);
            for (int m = 0; m < found.length; m++) {
                if (files[m].equals(i.getImageFile())) {
                    found[m] = true;
                }
            }
        }
        for (n = 0; n < found.length; n++) {
            assertTrue("Photo " + n + " not found", found[n]);
        }
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        PhotoInfo[] photosInTopFolder = { p1, p2 };
        assertFolderHasPhotos(topFolder, photosInTopFolder);
        PhotoFolder subFolder = topFolder.getSubfolder(0);
        assertEquals("Subfolder name not correct", "test", subFolder.getName());
        PhotoInfo[] photosInSubFolder = { p2 };
        assertFolderHasPhotos(subFolder, photosInSubFolder);
        assertEquals("Wrong photo count in listener", 2, l.photoCount);
        assertEquals("Wrong photo count in indexer statistics", 2, indexer.getNewPhotoCount());
        assertEquals("Wrong instance count in listener", 3, l.instanceCount);
        assertEquals("Wrong instance count in indexer statistics", 3, indexer.getNewInstanceCount());
        assertEquals("Indexing complete 100%", 100, indexer.getPercentComplete());
        assertNotNull("StartTime still null", indexer.getStartTime());
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        try {
            File testfile3 = new File("testfiles", "test3.jpg");
            File f3 = new File(extVolDir, "test3.jpg");
            FileUtils.copyFile(testfile3, f3);
            File f1 = new File(extVolDir, "test1.jpg");
            FileUtils.copyFile(testfile3, f1);
            File f2 = new File(extVolDir, "test2.jpg");
            f2.delete();
        } catch (IOException ex) {
            fail("IOException while altering external volume: " + ex.getMessage());
        }
        indexer = new ExtVolIndexer(v);
        indexer.setTopFolder(topFolder);
        l = new TestListener();
        indexer.addIndexerListener(l);
        assertEquals("Indexing not started -> completeness must be 0", 0, indexer.getPercentComplete());
        assertNull("StartTime must be null before starting", indexer.getStartTime());
        indexer.run();
        PhotoInfo[] photos3 = PhotoInfo.retrieveByOrigHash(hash3);
        assertEquals("1 photo per picture should be found", 1, photos3.length);
        PhotoInfo p3 = photos3[0];
        PhotoInfo photosInTopFolder2[] = { p3 };
        assertFolderHasPhotos(topFolder, photosInTopFolder2);
        assertEquals("More than 1 subfolder in topFolder", 1, topFolder.getSubfolderCount());
        subFolder = topFolder.getSubfolder(0);
        assertEquals("Subfolder name not correct", "test", subFolder.getName());
        PhotoInfo[] photosInSubFolder2 = { p2 };
        assertFolderHasPhotos(subFolder, photosInSubFolder2);
        Collection p2folders = p2.getFolders();
        assertFalse("p2 must not be in topFolder", p2folders.contains(topFolder));
    }
