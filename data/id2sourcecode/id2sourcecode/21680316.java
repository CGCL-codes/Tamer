    @Test
    public void testFilters() {
        OutgoingBatches batches = buildSampleBatches("testChannel", 5);
        Assert.assertNotNull(batches);
        Assert.assertEquals(25, batches.getBatches().size());
        Assert.assertEquals(0, batches.getActiveChannels().size());
        batches.filterBatchesForChannel("testChannel3");
        Assert.assertEquals(20, batches.getBatches().size());
        for (OutgoingBatch b : batches.getBatches()) {
            Assert.assertFalse(b.getChannelId().equals("testChannel3"));
        }
        batches.filterBatchesForChannel(new Channel("testChannel4", 1));
        Assert.assertEquals(15, batches.getBatches().size());
        for (OutgoingBatch b : batches.getBatches()) {
            Assert.assertFalse(b.getChannelId().equals("testChannel3"));
            Assert.assertFalse(b.getChannelId().equals("testChannel4"));
        }
        Set<String> channels = new HashSet<String>();
        channels.add("testChannel2");
        channels.add("testChannel5");
        batches.filterBatchesForChannels(channels);
        Assert.assertEquals(10, batches.getBatches().size());
        for (OutgoingBatch b : batches.getBatches()) {
            Assert.assertTrue(b.getChannelId().equals("testChannel1") || b.getChannelId().equals("testChannel0"));
        }
        batches = buildSampleBatches("testChannel", 5);
        batches.addActiveChannel(new NodeChannel("testChannel2"));
        batches.addActiveChannel(new NodeChannel("testChannel3"));
        batches.addActiveChannel(new NodeChannel("testChannel4"));
        batches.filterBatchesForInactiveChannels();
        Assert.assertEquals(15, batches.getBatches().size());
        for (OutgoingBatch b : batches.getBatches()) {
            Assert.assertTrue(b.getChannelId().equals("testChannel2") || b.getChannelId().equals("testChannel3") || b.getChannelId().equals("testChannel4"));
        }
    }
