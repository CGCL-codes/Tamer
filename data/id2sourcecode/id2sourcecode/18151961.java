    public Tape(File source, String encodingName) throws IOException {
        if (source == null) throw new NullPointerException("source");
        FileInputStream stream = new FileInputStream(source);
        ReadableByteChannel channel = (ReadableByteChannel) (stream.getChannel());
        Reader reader = Channels.newReader(channel, encodingName);
        this.source = reader;
        this.sourceName = source.getName();
    }
