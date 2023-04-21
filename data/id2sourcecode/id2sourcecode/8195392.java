    public ChannelWriter(File f) throws IOException {
        this(new FileOutputStream(f).getChannel());
    }
