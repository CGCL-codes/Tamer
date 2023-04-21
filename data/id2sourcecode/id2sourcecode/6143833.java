    @Test
    public void patchMultipleChannels() {
        Patch patch = new Patch(context);
        final Patcher patcher = new Patcher(context, patch);
        patcher.setUpdateLanbox(true);
        patcher.defaultPatchAndWait();
        List<Channel> channels = new ArrayList<Channel>();
        channels.add(context.getShow().getChannels().get(10));
        channels.add(context.getShow().getChannels().get(20));
        int[] patchDetailIndexes = { 1, 3, 5 };
        patcher.patchAndWait(patchDetailIndexes, channels);
        Dimmers dimmers = context.getShow().getDimmers();
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), 10);
        assertEquals(dimmers.get(2).getChannelId(), 20);
        assertEquals(dimmers.get(3).getChannelId(), 3);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(5).getChannelId(), 5);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 10);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 20);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 3);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
        assertEquals(dimmers.get(5).getLanboxChannelId(), 5);
        patcher.loadPatchAndWait(true);
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), 10);
        assertEquals(dimmers.get(2).getChannelId(), 20);
        assertEquals(dimmers.get(3).getChannelId(), 3);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(5).getChannelId(), 5);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 10);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 20);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 3);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
        assertEquals(dimmers.get(5).getLanboxChannelId(), 5);
    }
