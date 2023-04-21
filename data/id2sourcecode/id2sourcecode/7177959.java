    public void readThenWrite(long threshold) throws Exception {
        InputStream is = getClass().getResourceAsStream("FileCsvDataWriterTest.1.csv");
        String origCsv = IOUtils.toString(is);
        is.close();
        StagingManager stagingManager = new StagingManager(threshold, 0l, DIR.getAbsolutePath());
        ProtocolDataReader reader = new ProtocolDataReader(origCsv);
        StagingDataWriter writer = new StagingDataWriter("aaa", "test", stagingManager, new BatchListener());
        DataProcessor processor = new DataProcessor(reader, writer);
        processor.process();
        Assert.assertEquals(1, batchesWritten.size());
        Assert.assertEquals(origCsv, batchesWritten.get(0));
        StagedResource resource = (StagedResource) stagingManager.find("test", "aaa", 1);
        Assert.assertNotNull(resource);
        if (threshold > origCsv.length()) {
            Assert.assertFalse(resource.getFile().exists());
        } else {
            Assert.assertTrue(resource.getFile().exists());
        }
        resource.delete();
        Assert.assertFalse(resource.getFile().exists());
    }
