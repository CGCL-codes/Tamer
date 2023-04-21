    @Test()
    public void testGetSuspendIgnoreChannels() throws Exception {
        String nodeId = "00000";
        ChannelMap result = configurationService.getSuspendIgnoreChannelLists(nodeId);
        Assert.assertEquals(result.getSuspendChannels().size(), 0);
        Assert.assertEquals(result.getIgnoreChannels().size(), 0);
        ConfigurationService configurationService = (ConfigurationService) find(Constants.CONFIG_SERVICE);
        List<NodeChannel> ncs = configurationService.getNodeChannels(nodeId, false);
        NodeChannel nc = ncs.get(1);
        nc.setSuspendEnabled(true);
        configurationService.saveNodeChannelControl(nc, false);
        result = configurationService.getSuspendIgnoreChannelLists(nodeId);
        Assert.assertTrue(result.getSuspendChannels().contains(nc.getChannelId()));
        nc = ncs.get(0);
        nc.setSuspendEnabled(true);
        configurationService.saveNodeChannelControl(nc, false);
        result = configurationService.getSuspendIgnoreChannelLists(nodeId);
        Assert.assertTrue(result.getSuspendChannels().contains(ncs.get(0).getChannelId()));
        Assert.assertTrue(result.getSuspendChannels().contains(ncs.get(1).getChannelId()));
        nc.setIgnoreEnabled(true);
        configurationService.saveNodeChannelControl(nc, false);
        result = configurationService.getSuspendIgnoreChannelLists(nodeId);
        Assert.assertTrue(result.getSuspendChannels().contains(ncs.get(0).getChannelId()));
        Assert.assertTrue(result.getSuspendChannels().contains(ncs.get(1).getChannelId()));
        Assert.assertTrue(result.getIgnoreChannels().contains(nc.getChannelId()));
    }
