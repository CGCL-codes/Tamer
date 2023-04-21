    @Test
    public void defaultPatchAndClearPatch() {
        Dimmer dimmer = tester.getContext().getShow().getDimmers().get(0);
        defaultPatch();
        assertEquals(dimmer.getChannelId(), 0);
        clearPatch();
        assertEquals(dimmer.getChannelId(), -1);
        defaultPatch();
        assertEquals(dimmer.getChannelId(), 0);
    }
