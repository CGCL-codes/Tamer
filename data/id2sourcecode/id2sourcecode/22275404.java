    public ShortFileChannel(final String sourceFileName, final SampleSign sign, final ByteOrder order) throws IOException {
        this.file = new RandomAccessFile(sourceFileName, "r");
        if ((this.file.length() % 2) != 0) {
            file.close();
            throw new IOException("File length does not match");
        }
        this.channel = file.getChannel();
        this.sign = sign;
        this.order = order;
    }
