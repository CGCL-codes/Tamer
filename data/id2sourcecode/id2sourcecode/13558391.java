    @Test
    public void parseGroup() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <groups>\n" + "    <group name=\"Group 1\"/>\n" + "    <group name=\"Group 2\">\n" + "      <comment>\n" + "        <line text=\"comment line 1\"/>\n" + "        <line text=\"comment line 2\"/>\n" + "      </comment>\n" + "      <fixtures>\n" + "        <fixture id=\"1\"/>\n" + "      </fixtures>\n" + "    </group>\n" + "    <group name=\"Group 3\">\n" + "      <fixtures>\n" + "        <fixture id=\"1\"/>\n" + "        <fixture id=\"2\"/>\n" + "        <fixture id=\"3\"/>\n" + "      </fixtures>\n" + "    </group>\n" + "  </groups>\n" + "</show>\n";
        Show show = toShow(xml);
        Groups groups = show.getGroups();
        assertEquals(groups.size(), 3);
        Group group1 = groups.get(0);
        Group group2 = groups.get(1);
        Group group3 = groups.get(2);
        assertEquals(group1.getName(), "Group 1");
        assertEquals(group2.getName(), "Group 2");
        assertEquals(group3.getName(), "Group 3");
        assertEquals(group2.getComment(), "comment line 1\ncomment line 2");
        assertTrue(group1.getDirty() == dirty);
        assertTrue(group2.getDirty() == dirty);
        assertTrue(group3.getDirty() == dirty);
        Channel[] channels1 = group1.getChannels();
        Channel[] channels2 = group2.getChannels();
        Channel[] channels3 = group3.getChannels();
        assertEquals(channels1.length, 0);
        assertEquals(channels2.length, 1);
        assertEquals(channels3.length, 3);
        assertEquals(channels2[0].getName(), "Channel 1");
        assertEquals(channels3[0].getName(), "Channel 1");
        assertEquals(channels3[1].getName(), "Channel 2");
        assertEquals(channels3[2].getName(), "Channel 3");
    }
