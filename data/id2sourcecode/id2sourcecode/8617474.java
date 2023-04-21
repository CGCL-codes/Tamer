    @Test
    public void testGetNodeChannelsById() throws Exception {
        String nodeId = "12345";
        List<NodeChannel> nodeChannels = configurationService.getNodeChannels(nodeId, false);
        Assert.assertNotNull(nodeChannels);
        Assert.assertEquals(4, nodeChannels.size());
        for (NodeChannel nc : nodeChannels) {
            Assert.assertTrue(nodeId.equals(nc.getNodeId()));
            Assert.assertFalse(nc.isIgnoreEnabled());
            Assert.assertFalse(nc.isSuspendEnabled());
            Assert.assertNull(nc.getLastExtractedTime());
        }
        NodeChannel nc = nodeChannels.get(0);
        String updatedChannelId = nc.getChannelId();
        nc.setIgnoreEnabled(true);
        configurationService.saveNodeChannelControl(nc, false);
        NodeChannel compareTo = configurationService.getNodeChannel(updatedChannelId, nodeId, false);
        Assert.assertTrue(compareTo.isIgnoreEnabled());
        Assert.assertFalse(compareTo.isSuspendEnabled());
        Assert.assertNull(compareTo.getLastExtractedTime());
        compareTo.setSuspendEnabled(true);
        configurationService.saveNodeChannelControl(compareTo, false);
        compareTo = configurationService.getNodeChannel(updatedChannelId, nodeId, false);
        Assert.assertTrue(compareTo.isIgnoreEnabled());
        Assert.assertTrue(compareTo.isSuspendEnabled());
        Assert.assertNull(compareTo.getLastExtractedTime());
        NodeChannel nc1 = nodeChannels.get(1);
        String updatedChannelId1 = nc1.getChannelId();
        Date date = new Date();
        nc1.setLastExtractedTime(date);
        configurationService.saveNodeChannelControl(nc1, false);
        compareTo = configurationService.getNodeChannel(updatedChannelId1, nodeId, false);
        Assert.assertFalse(compareTo.isIgnoreEnabled());
        Assert.assertFalse(compareTo.isSuspendEnabled());
        Assert.assertNotNull(compareTo.getLastExtractedTime());
        Assert.assertEquals(date.getTime(), compareTo.getLastExtractedTime().getTime());
        compareTo = configurationService.getNodeChannel(updatedChannelId, nodeId, false);
        Assert.assertTrue(compareTo.isIgnoreEnabled());
        Assert.assertTrue(compareTo.isSuspendEnabled());
        Assert.assertNull(compareTo.getLastExtractedTime());
    }
