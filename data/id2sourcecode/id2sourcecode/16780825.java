    public ChannelReader(File f) throws IOException {
        this(new FileInputStream(f).getChannel());
    }
