    private void checkAudioFormat(AudioFormat audioFormat) throws Exception {
        assertEquals("encoding", getEncoding(), audioFormat.getEncoding());
        assertEquals("sample rate", getSampleRate(), audioFormat.getSampleRate(), DELTA);
        assertEquals("sample size (bits)", getSampleSizeInBits(), audioFormat.getSampleSizeInBits());
        assertEquals("channels", getChannels(), audioFormat.getChannels());
        assertEquals("frame size", getFrameSize(), audioFormat.getFrameSize());
        assertEquals("frame rate", getFrameRate(), audioFormat.getFrameRate(), DELTA);
        assertEquals("big endian", getBigEndian(), audioFormat.isBigEndian());
    }
