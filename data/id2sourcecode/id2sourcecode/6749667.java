    @Test
    public void saveToLanboxAndLoadFromLanbox() {
        Dimmer dimmer = tester.getContext().getShow().getDimmers().get(0);
        defaultPatch();
        assertEquals(dimmer.getChannelId(), 0);
        tester.pushMenu("File|Save to Lanbox");
        Util.sleep(500);
        assertEquals(dimmer.getChannelId(), 0);
        clearPatch();
        assertEquals(dimmer.getChannelId(), -1);
        tester.pushMenu("File|Load from Lanbox");
        Util.sleep(500);
        assertEquals(dimmer.getChannelId(), 0);
    }
