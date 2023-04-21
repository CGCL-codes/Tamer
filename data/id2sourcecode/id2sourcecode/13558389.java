    @Test
    public void parseChannel() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "  </fixtures>\n" + "</show>\n";
        Show show = toShow(xml);
        Channel channel1 = show.getChannels().get(0);
        Channel channel2 = show.getChannels().get(1);
        assertEquals(channel1.getName(), "Channel 1");
        assertEquals(channel2.getName(), "Channel 2");
        assertEquals(channel1.getId(), 0);
        assertEquals(channel2.getId(), 1);
        assertTrue(channel1.getDirty() == dirty);
        assertTrue(channel2.getDirty() == dirty);
    }
