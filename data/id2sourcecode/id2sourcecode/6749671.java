    @Test
    public void copyDimmerNamesToChannelNames() {
        Dimmer dimmer = tester.getContext().getShow().getDimmers().get(0);
        Channel channel = tester.getContext().getShow().getChannels().get(0);
        defaultPatch();
        dimmer.setName("Dimmer 1");
        tester.pushMenu("Actions|Copy dimmer names to channel names");
        Util.sleep(500);
        assertEquals(channel.getName(), "Dimmer 1");
    }
