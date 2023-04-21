    @Test
    public void patchSingleChannelToMultipleDimmers() {
        Patch patch = new Patch(context);
        final Patcher patcher = new Patcher(context, patch);
        patcher.setUpdateLanbox(true);
        patcher.defaultPatchAndWait();
        Channel channel = context.getShow().getChannels().get(5);
        List<Channel> channels = new ArrayList<Channel>();
        channels.add(channel);
        int[] patchDetailIndexes = { 1, 3 };
        patcher.patchAndWait(patchDetailIndexes, channels);
        Dimmers dimmers = context.getShow().getDimmers();
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), 5);
        assertEquals(dimmers.get(2).getChannelId(), 2);
        assertEquals(dimmers.get(3).getChannelId(), 5);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 5);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 2);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 5);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
        patcher.loadPatchAndWait(true);
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), 5);
        assertEquals(dimmers.get(2).getChannelId(), 2);
        assertEquals(dimmers.get(3).getChannelId(), 5);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 5);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 2);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 5);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
    }
