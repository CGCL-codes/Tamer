    @Test
    public void testParse() throws Exception {
        final URL url = new URL("playback://audio?rate=8000&channels=2&encoding=pcm");
        AudioFormat format = JavaSoundParser.parse(url);
        Assert.assertEquals(new Float(8000.0), new Float(format.getSampleRate()));
        Assert.assertEquals(2, format.getChannels());
        Assert.assertEquals(AudioFormat.Encoding.PCM_SIGNED, format.getEncoding());
    }
