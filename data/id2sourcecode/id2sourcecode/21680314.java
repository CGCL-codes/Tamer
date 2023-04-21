    @Test
    public void testBasicGetters() {
        OutgoingBatches batches = buildSampleBatches("testChannel", 5);
        Assert.assertNotNull(batches);
        Assert.assertEquals(25, batches.getBatches().size());
        Assert.assertEquals(0, batches.getActiveChannels().size());
        List<OutgoingBatch> batchList = batches.getBatchesForChannel("testChannel2");
        Assert.assertEquals(5, batchList.size());
        int i = 3;
        for (OutgoingBatch b : batchList) {
            Assert.assertEquals(b.getChannelId(), "testChannel2");
            Assert.assertEquals(i, b.getBatchId());
            i += 5;
        }
        batchList = batches.getBatchesForChannel(new Channel("testChannel1", 1));
        Assert.assertEquals(5, batchList.size());
        i = 2;
        for (OutgoingBatch b : batchList) {
            Assert.assertEquals(b.getChannelId(), "testChannel1");
            Assert.assertEquals(i, b.getBatchId());
            i += 5;
        }
        Set<String> channels = new HashSet<String>();
        channels.add("testChannel2");
        channels.add("testChannel3");
        channels.add("testChannel4");
        batchList = batches.getBatchesForChannels(channels);
        Assert.assertEquals(15, batchList.size());
    }
