    @Test
    public void unPatch() {
        Patch patch = new Patch(context);
        final Patcher patcher = new Patcher(context, patch);
        patcher.setUpdateLanbox(true);
        patcher.defaultPatchAndWait();
        int[] patchDetailIndexes = { 1, 3 };
        patcher.unpatchAndWait(patchDetailIndexes);
        Dimmers dimmers = context.getShow().getDimmers();
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), -1);
        assertEquals(dimmers.get(2).getChannelId(), 2);
        assertEquals(dimmers.get(3).getChannelId(), -1);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 257);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 2);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 259);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
        patcher.loadPatchAndWait(true);
        assertEquals(dimmers.get(0).getChannelId(), 0);
        assertEquals(dimmers.get(1).getChannelId(), -1);
        assertEquals(dimmers.get(2).getChannelId(), 2);
        assertEquals(dimmers.get(3).getChannelId(), -1);
        assertEquals(dimmers.get(4).getChannelId(), 4);
        assertEquals(dimmers.get(0).getLanboxChannelId(), 0);
        assertEquals(dimmers.get(1).getLanboxChannelId(), 257);
        assertEquals(dimmers.get(2).getLanboxChannelId(), 2);
        assertEquals(dimmers.get(3).getLanboxChannelId(), 259);
        assertEquals(dimmers.get(4).getLanboxChannelId(), 4);
    }
