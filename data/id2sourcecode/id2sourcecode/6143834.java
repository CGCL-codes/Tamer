    @Test
    public void savePatch() {
        Patch patch = new Patch(context);
        final Patcher patcher = new Patcher(context, patch);
        patcher.setUpdateLanbox(true);
        patcher.defaultPatchAndWait();
        patcher.setUpdateLanbox(false);
        List<Channel> channels = new ArrayList<Channel>();
        channels.add(context.getShow().getChannels().get(10));
        int[] patchDetailIndexes = { 5 };
        patcher.patchAndWait(patchDetailIndexes, channels);
        Dimmer dimmer = context.getShow().getDimmers().get(5);
        assertEquals(dimmer.getChannelId(), 10);
        assertEquals(dimmer.getLanboxChannelId(), 5);
        patcher.loadPatchAndWait(true);
        assertEquals(dimmer.getChannelId(), 5);
        assertEquals(dimmer.getLanboxChannelId(), 5);
        patcher.patchAndWait(patchDetailIndexes, channels);
        patcher.savePatchAndWait();
        assertEquals(dimmer.getChannelId(), 10);
        assertEquals(dimmer.getLanboxChannelId(), 10);
        patcher.loadPatchAndWait(true);
        assertEquals(dimmer.getChannelId(), 10);
        assertEquals(dimmer.getLanboxChannelId(), 10);
    }
