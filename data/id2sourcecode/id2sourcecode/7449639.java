    private void checkAudioFormat(AudioFormat audioFormat, String strMessagePrefix) throws Exception {
        assertEquals(strMessagePrefix + "encoding", getEncoding(), audioFormat.getEncoding());
        assertEquals(strMessagePrefix + "sample rate", getSampleRate(), audioFormat.getSampleRate(), DELTA);
        assertEquals(strMessagePrefix + "sample size (bits)", getSampleSizeInBits(), audioFormat.getSampleSizeInBits());
        assertEquals(strMessagePrefix + "channels", getChannels(), audioFormat.getChannels());
        assertEquals(strMessagePrefix + "frame size", getFrameSize(), audioFormat.getFrameSize());
        assertEquals(strMessagePrefix + "frame rate", getFrameRate(), audioFormat.getFrameRate(), DELTA);
        assertEquals(strMessagePrefix + "big endian", getBigEndian(), audioFormat.isBigEndian());
    }
