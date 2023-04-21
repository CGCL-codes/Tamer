    public void testConstructors() {
        {
            final WavAudioFormat f = new WavAudioFormat("abc");
            assertEquals(f.getChannels(), -1);
            assertEquals(f.getDataType(), byte[].class);
            assertEquals(f.getEncoding(), "abc");
            assertEquals(f.getEndian(), -1);
            assertEquals(f.getFrameRate(), -1.0);
            assertEquals(f.getFrameSizeInBits(), -1);
            assertEquals(f.getSampleRate(), -1.0);
            assertEquals(f.getSampleSizeInBits(), -1);
            assertEquals(f.getSigned(), -1);
        }
    }
