    @Test
    public void parseDimmer() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <patch-lines>\n" + "    <patch-line id=\"1\" name=\"Dimmer 1\" fixture-id=\"2\"/>\n" + "    <patch-line id=\"2\" name=\"Dimmer 2\" fixture-id=\"1\"/>\n" + "    <patch-line id=\"3\" name=\"Dimmer 3\" fixture-id=\"1\"/>\n" + "    <patch-line id=\"4\" name=\"Dimmer 4\"/>\n" + "  </patch-lines>\n" + "</show>\n";
        Show show = toShow(xml);
        Channel channel1 = show.getChannels().get(0);
        Channel channel2 = show.getChannels().get(1);
        Channel channel3 = show.getChannels().get(2);
        Dimmer dimmer1 = show.getDimmers().get(0);
        Dimmer dimmer2 = show.getDimmers().get(1);
        Dimmer dimmer3 = show.getDimmers().get(2);
        Dimmer dimmer4 = show.getDimmers().get(3);
        assertEquals(dimmer1.getName(), "Dimmer 1");
        assertEquals(dimmer2.getName(), "Dimmer 2");
        assertEquals(dimmer3.getName(), "Dimmer 3");
        assertEquals(dimmer4.getName(), "Dimmer 4");
        assertEquals(dimmer1.getId(), 0);
        assertEquals(dimmer2.getId(), 1);
        assertEquals(dimmer3.getId(), 2);
        assertEquals(dimmer4.getId(), 3);
        assertTrue(dimmer1.getDirty() == dirty);
        assertTrue(dimmer2.getDirty() == dirty);
        assertTrue(dimmer3.getDirty() == dirty);
        assertTrue(dimmer4.getDirty() == dirty);
        assertEquals(dimmer1.getChannelId(), 1);
        assertEquals(dimmer2.getChannelId(), 0);
        assertEquals(dimmer3.getChannelId(), 0);
        assertEquals(dimmer4.getChannelId(), -1);
        assertTrue(channel1.isPatched());
        assertTrue(channel2.isPatched());
        assertFalse(channel3.isPatched());
    }
