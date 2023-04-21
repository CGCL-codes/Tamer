    public void testAudioFormat() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.ALAW, 1f, 2, 3, 4, 5f, true);
        assertEquals(AudioFormat.Encoding.ALAW, format.getEncoding());
        assertEquals(1f, format.getSampleRate());
        assertEquals(2, format.getSampleSizeInBits());
        assertEquals(3, format.getChannels());
        assertEquals(4, format.getFrameSize());
        assertEquals(5f, format.getFrameRate());
        assertTrue(format.isBigEndian());
        assertTrue(format.properties().isEmpty());
        HashMap<String, Object> prop = new HashMap<String, Object>();
        prop.put("bitrate", Integer.valueOf(100));
        prop.put("vbr", Boolean.TRUE);
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 5f, 4, 3, 2, 1f, false, prop);
        assertEquals(5f, format.getSampleRate());
        assertEquals(4, format.getSampleSizeInBits());
        assertEquals(3, format.getChannels());
        assertEquals(2, format.getFrameSize());
        assertEquals(1f, format.getFrameRate());
        assertFalse(format.isBigEndian());
        assertEquals(2, format.properties().size());
        assertEquals(Integer.valueOf(100), format.properties().get("bitrate"));
        assertEquals(Boolean.TRUE, format.properties().get("vbr"));
        try {
            format.properties().put("aa", 1);
            fail("No expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
        format = new AudioFormat(1f, 10, 2, true, false);
        assertEquals(AudioFormat.Encoding.PCM_SIGNED, format.getEncoding());
        assertEquals(1f, format.getSampleRate());
        assertEquals(10, format.getSampleSizeInBits());
        assertEquals(2, format.getChannels());
        assertEquals(4, format.getFrameSize());
        assertEquals(1f, format.getFrameRate());
        assertFalse(format.isBigEndian());
        assertTrue(format.properties().isEmpty());
    }
