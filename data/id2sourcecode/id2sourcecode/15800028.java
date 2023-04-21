    @Test
    public void testParseNewSDTV() {
        String filename = "/parsertestdata/new/sdtv/info";
        File file = this.urlToFile(filename);
        Recording recording = null;
        try {
            recording = recordingParser.parse(file);
        } catch (VDRParserException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println(recording);
        assertEquals("Das Erste", recording.getChannelName());
        assertEquals("Tagesschau", recording.getTitle());
        assertEquals(null, recording.getDescription());
        assertEquals(null, recording.getShortText());
        assertEquals(1, recording.getParts());
        assertEquals(file.getParent(), recording.getPath());
        assertTrue(recording.isTs());
        List<StreamInfo> streamInfos = recording.getStreamInfos();
        assertEquals(4, streamInfos.size());
        assertEquals(1, streamInfos.get(0).getStream());
        assertEquals("03", streamInfos.get(0).getType());
        assertEquals("deu", streamInfos.get(0).getLang());
        assertEquals(2, streamInfos.get(1).getStream());
        assertEquals("03", streamInfos.get(1).getType());
        assertEquals("deu", streamInfos.get(1).getLang());
        assertEquals(2, streamInfos.get(2).getStream());
        assertEquals("05", streamInfos.get(2).getType());
        assertEquals("deu", streamInfos.get(2).getLang());
        assertEquals(2, streamInfos.get(3).getStream());
        assertEquals("40", streamInfos.get(3).getType());
        assertEquals("deu", streamInfos.get(3).getLang());
        assertEquals(XmlUtilities.getXmlGregorianCalendar(1258462800), recording.getStartDate());
        assertEquals(XmlUtilities.getXmlDuration(600), recording.getDuration());
        assertEquals(XmlUtilities.getXmlDuration(0), recording.getTotalDuration());
    }
