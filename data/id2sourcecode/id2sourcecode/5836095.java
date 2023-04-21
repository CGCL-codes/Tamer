    private void dumpAudioFileFormat(AudioFileFormat baseFileFormat, PrintStream out, String info) throws UnsupportedAudioFileException {
        AudioFormat baseFormat = baseFileFormat.getFormat();
        if (out != null) {
            out.println("  -----  " + info + "  -----");
            out.println("    ByteLength=" + baseFileFormat.getByteLength());
            out.println("    FrameLength=" + baseFileFormat.getFrameLength());
            out.println("    Type=" + baseFileFormat.getType());
            out.println("    SourceFormat=" + baseFormat.toString());
            out.println("    Channels=" + baseFormat.getChannels());
            out.println("    FrameRate=" + baseFormat.getFrameRate());
            out.println("    FrameSize=" + baseFormat.getFrameSize());
            out.println("    SampleRate=" + baseFormat.getSampleRate());
            out.println("    SampleSizeInBits=" + baseFormat.getSampleSizeInBits());
            out.println("    Encoding=" + baseFormat.getEncoding());
        }
        assertEquals("Type", (String) props.getProperty("Type"), baseFileFormat.getType().toString());
        assertEquals("SourceFormat", (String) props.getProperty("SourceFormat"), baseFormat.toString());
        assertEquals("Channels", Integer.parseInt((String) props.getProperty("Channels")), baseFormat.getChannels());
        assertTrue("FrameRate", Float.parseFloat((String) props.getProperty("FrameRate")) == baseFormat.getFrameRate());
        assertEquals("FrameSize", Integer.parseInt((String) props.getProperty("FrameSize")), baseFormat.getFrameSize());
        assertTrue("SampleRate", Float.parseFloat((String) props.getProperty("SampleRate")) == baseFormat.getSampleRate());
        assertEquals("SampleSizeInBits", Integer.parseInt((String) props.getProperty("SampleSizeInBits")), baseFormat.getSampleSizeInBits());
        assertEquals("Encoding", (String) props.getProperty("Encoding"), baseFormat.getEncoding().toString());
    }
