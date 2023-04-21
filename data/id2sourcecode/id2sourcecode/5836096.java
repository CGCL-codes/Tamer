    private void dumpAudioInputStream(AudioInputStream in, PrintStream out, String info) throws IOException {
        AudioFormat baseFormat = in.getFormat();
        if (out != null) {
            out.println("  -----  " + info + "  -----");
            out.println("    Available=" + in.available());
            out.println("    FrameLength=" + in.getFrameLength());
            out.println("    SourceFormat=" + baseFormat.toString());
            out.println("    Channels=" + baseFormat.getChannels());
            out.println("    FrameRate=" + baseFormat.getFrameRate());
            out.println("    FrameSize=" + baseFormat.getFrameSize());
            out.println("    SampleRate=" + baseFormat.getSampleRate());
            out.println("    SampleSizeInBits=" + baseFormat.getSampleSizeInBits());
            out.println("    Encoding=" + baseFormat.getEncoding());
        }
        assertEquals("SourceFormat", (String) props.getProperty("SourceFormat"), baseFormat.toString());
        assertEquals("Channels", Integer.parseInt((String) props.getProperty("Channels")), baseFormat.getChannels());
        assertTrue("FrameRate", Float.parseFloat((String) props.getProperty("FrameRate")) == baseFormat.getFrameRate());
        assertEquals("FrameSize", Integer.parseInt((String) props.getProperty("FrameSize")), baseFormat.getFrameSize());
        assertTrue("SampleRate", Float.parseFloat((String) props.getProperty("SampleRate")) == baseFormat.getSampleRate());
        assertEquals("SampleSizeInBits", Integer.parseInt((String) props.getProperty("SampleSizeInBits")), baseFormat.getSampleSizeInBits());
        assertEquals("Encoding", (String) props.getProperty("Encoding"), baseFormat.getEncoding().toString());
    }
