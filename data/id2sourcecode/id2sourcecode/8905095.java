    public void testExportImport() throws ZoieException, IOException {
        File idxDir = getIdxDir();
        ZoieSystem<IndexReader, String> idxSystem = createZoie(idxDir, true);
        idxSystem.start();
        DirectoryManager dirMgr = new DefaultDirectoryManager(idxDir);
        String query = "zoie";
        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "contents", idxSystem.getAnalyzer());
        Query q = null;
        try {
            q = parser.parse(query);
        } catch (ParseException e) {
            throw new ZoieException(e.getMessage(), e);
        }
        try {
            List<DataEvent<String>> list;
            IndexSignature sig;
            list = new ArrayList<DataEvent<String>>(TestData.testdata.length);
            for (int i = 0; i < TestData.testdata.length; ++i) {
                list.add(new DataEvent<String>(i, TestData.testdata[i]));
            }
            idxSystem.consume(list);
            idxSystem.flushEvents(100000);
            sig = dirMgr.getCurrentIndexSignature();
            assertEquals("index version mismatch after first flush", TestData.testdata.length - 1, sig.getVersion());
            long versionExported = sig.getVersion();
            int hits = countHits(idxSystem, q);
            RandomAccessFile exportFile;
            FileChannel channel;
            exportFile = new RandomAccessFile(new File(getTmpDir(), "zoie_export.dat"), "rw");
            channel = exportFile.getChannel();
            idxSystem.exportSnapshot(channel);
            exportFile.close();
            exportFile = null;
            channel = null;
            list = new ArrayList<DataEvent<String>>(TestData.testdata.length);
            for (int i = 0; i < TestData.testdata2.length; ++i) {
                list.add(new DataEvent<String>(TestData.testdata.length + i, TestData.testdata2[i]));
            }
            idxSystem.consume(list);
            idxSystem.flushEvents(100000);
            sig = dirMgr.getCurrentIndexSignature();
            assertEquals("index version mismatch after second flush", TestData.testdata.length + TestData.testdata2.length - 1, sig.getVersion());
            assertEquals("should have no hits", 0, countHits(idxSystem, q));
            exportFile = new RandomAccessFile(new File(getTmpDir(), "zoie_export.dat"), "r");
            channel = exportFile.getChannel();
            idxSystem.importSnapshot(channel);
            exportFile.close();
            assertEquals("count is wrong", hits, countHits(idxSystem, q));
            sig = dirMgr.getCurrentIndexSignature();
            assertEquals("imported version is wrong", versionExported, sig.getVersion());
        } catch (ZoieException e) {
            throw e;
        } finally {
            idxSystem.shutdown();
            deleteDirectory(idxDir);
        }
    }
