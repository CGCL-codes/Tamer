    @Test
    public void testAddOutstandingInterest() throws Exception {
        ContentName streamName = ContentName.fromNative(testHelper.getTestNamespace("testAddOutstandingInterest"), "testFile.bin");
        writeHandle.registerFilter(streamName, this);
        CCNVersionedInputStream vis = new CCNVersionedInputStream(streamName, readHandle);
        byte[] resultDigest = readFile(vis);
        Log.info("Finished reading, read result {0}", DataUtils.printHexBytes(resultDigest));
        if (!writer.isDone()) {
            synchronized (writer) {
                while (!writer.isDone()) {
                    writer.wait(500);
                }
            }
        }
        Log.info("Finished writing, read result {0}, write result {1}", DataUtils.printHexBytes(resultDigest), DataUtils.printHexBytes(writeDigest));
        Assert.assertArrayEquals(resultDigest, writeDigest);
        Assert.assertArrayEquals(writer.getFirstDigest(), vis.getFirstDigest());
        Assert.assertEquals(writer.firstSegmentNumber(), (Long) vis.firstSegmentNumber());
        readHandle.close();
        writeHandle.close();
    }
