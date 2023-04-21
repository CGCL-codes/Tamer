    @Test
    public void parseCueList() {
        String xml = "<show name=\"test\">\n" + "  <fixtures>\n" + "    <fixture id=\"1\" name=\"Channel 1\"/>\n" + "    <fixture id=\"2\" name=\"Channel 2\"/>\n" + "    <fixture id=\"3\" name=\"Channel 3\"/>\n" + "  </fixtures>\n" + "  <submasters>\n" + "    <submaster id=\"1\" name=\"Submaster 1\"/>\n" + "    <submaster id=\"2\" name=\"Submaster 2\">\n" + "      <scene>\n" + "        <set fixture-id=\"1\" attribute=\"Intensity\" value=\"100\"/>\n" + "        <set fixture-id=\"2\" attribute=\"Intensity\" value=\"50\"/>\n" + "      </scene>\n" + "    </submaster>\n" + "  </submasters>\n" + "  <cue-lists>\n" + "    <cue-list id=\"1\" name=\"Cue list 1\">\n" + "      <comment>\n" + "        <line text=\"cuelist comment line 1\"/>\n" + "        <line text=\"cuelist comment line 2\"/>\n" + "      </comment>\n" + "      <cue number=\"1.1\" page=\"page\" prompt=\"prompt\" description=\"L 2\">\n" + "        <comment>\n" + "          <line text=\"cue comment line 1\"/>\n" + "          <line text=\"cue comment line 2\"/>\n" + "        </comment>\n" + "        <submasters>\n" + "          <submaster id=\"1\" value=\"derived\"/>\n" + "          <submaster id=\"2\" value=\"77\"/>\n" + "        </submasters>\n" + "        <scene>\n" + "          <set fixture-id=\"1\" attribute=\"Intensity\" value=\"50\"/>\n" + "          <set fixture-id=\"2\" attribute=\"Intensity\" value=\"derived\"/>\n" + "        </scene>\n" + "      </cue>\n" + "    </cue-list>\n" + "  </cue-lists>\n" + "</show>\n";
        Show show = toShow(xml);
        assertEquals(show.getCues().size(), 1);
        Cue cue = show.getCues().get(0);
        assertTrue(cue.getDirty() == dirty);
        assertEquals(cue.getNumber(), "1.1");
        assertEquals(cue.getPage(), "page");
        assertEquals(cue.getPrompt(), "cue comment line 1\ncue comment line 2");
        assertEquals(cue.getDescription(), "L 2");
        LightCueDetail detail = (LightCueDetail) cue.getDetail();
        CueSubmasterLevel submasterLevel1 = detail.getSubmasterLevel(0);
        CueSubmasterLevel submasterLevel2 = detail.getSubmasterLevel(1);
        assertTrue(submasterLevel1.isActive());
        assertTrue(submasterLevel1.isDerived());
        assertTrue(submasterLevel2.isActive());
        assertFalse(submasterLevel2.isDerived());
        assertEquals(submasterLevel2.getIntValue(), 77);
        CueChannelLevel channelLevel1 = detail.getChannelLevel(0);
        CueChannelLevel channelLevel2 = detail.getChannelLevel(1);
        CueChannelLevel channelLevel3 = detail.getChannelLevel(2);
        assertTrue(channelLevel1.isActive());
        assertTrue(channelLevel2.isActive());
        assertFalse(channelLevel3.isActive());
        assertFalse(channelLevel1.isDerived());
        assertTrue(channelLevel2.isDerived());
        assertFalse(channelLevel3.isDerived());
        assertEquals(channelLevel1.getChannelIntValue(), 50);
        assertEquals(channelLevel2.getChannelIntValue(), 0);
        assertEquals(channelLevel3.getChannelIntValue(), 0);
    }
