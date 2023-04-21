    @Test
    public void testChannelSortingNoErrors() {
        List<NodeChannel> channels = new ArrayList<NodeChannel>();
        NodeChannel channelA = new NodeChannel("a");
        channelA.setProcessingOrder(1);
        NodeChannel channelB = new NodeChannel("b");
        channelB.setProcessingOrder(2);
        NodeChannel channelC = new NodeChannel("c");
        channelC.setProcessingOrder(3);
        channels.add(channelC);
        channels.add(channelB);
        channels.add(channelA);
        List<OutgoingBatch> batches = new ArrayList<OutgoingBatch>();
        OutgoingBatch batch1 = new OutgoingBatch("1", channelA.getChannelId(), Status.NE);
        batches.add(batch1);
        OutgoingBatch batch2 = new OutgoingBatch("1", channelB.getChannelId(), Status.NE);
        batches.add(batch2);
        OutgoingBatch batch3 = new OutgoingBatch("1", channelC.getChannelId(), Status.NE);
        batches.add(batch3);
        OutgoingBatches outgoingBatches = new OutgoingBatches(batches);
        outgoingBatches.sortChannels(channels);
        Assert.assertEquals(channelA, channels.get(0));
        Assert.assertEquals(channelB, channels.get(1));
        Assert.assertEquals(channelC, channels.get(2));
    }
