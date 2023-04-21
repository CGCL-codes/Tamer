    private EbmlFile(FileInputStream stream, boolean manageStream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream is null");
        }
        this.stream = stream;
        this.manageStream = manageStream;
        channel = stream.getChannel();
        manageChannel = true;
        reader = new EbmlFileReader(channel);
    }
