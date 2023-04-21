    @Test
    public void testParseOldZDF() {
        String filename = "/parsertestdata/old/zdf/info.vdr";
        File file = this.urlToFile(filename);
        Recording recording = null;
        try {
            recording = recordingParser.parse(file);
        } catch (VDRParserException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println(recording);
        assertEquals("ZDF", recording.getChannelName());
        assertEquals("ZDF in concert Robbie Williams in London", recording.getTitle());
        String desc = "Drei Jahre hat er die Bühne gemieden, jetzt ist er wieder da! Robbie Williams hat am 20. Oktober 2009 ein aufsehenerregendes Comeback mit einem Konzert im Londoner Roundhouse gefeiert. Das ZDF zeigt im Rahmen von \"ZDF in concert\" exklusiv einen 60-minütigen Zusammenschnitt mit allen Höhepunkten dieses einmaligen Konzertes. Deutschland, 2009";
        assertEquals(desc, recording.getDescription());
        assertEquals("Kurzer Titel des Konzerts", recording.getShortText());
        assertEquals(2, recording.getParts());
        assertEquals(file.getParent(), recording.getPath());
        assertFalse(recording.isTs());
        List<StreamInfo> streamInfos = recording.getStreamInfos();
        assertEquals(5, streamInfos.size());
        assertEquals(2, streamInfos.get(0).getStream());
        assertEquals("03", streamInfos.get(0).getType());
        assertEquals("deu", streamInfos.get(0).getLang());
        assertEquals(2, streamInfos.get(1).getStream());
        assertEquals("03", streamInfos.get(1).getType());
        assertEquals("deu", streamInfos.get(1).getLang());
        assertEquals(2, streamInfos.get(2).getStream());
        assertEquals("05", streamInfos.get(2).getType());
        assertEquals("deu", streamInfos.get(2).getLang());
        assertEquals(1, streamInfos.get(3).getStream());
        assertEquals("03", streamInfos.get(3).getType());
        assertEquals("deu", streamInfos.get(3).getLang());
        assertEquals(3, streamInfos.get(4).getStream());
        assertEquals("03", streamInfos.get(4).getType());
        assertEquals("deu", streamInfos.get(4).getLang());
        assertEquals(XmlUtilities.getXmlGregorianCalendar(1257637800), recording.getStartDate());
        assertEquals(XmlUtilities.getXmlDuration(3600), recording.getDuration());
        assertEquals(XmlUtilities.getXmlDuration(4320), recording.getTotalDuration());
    }
