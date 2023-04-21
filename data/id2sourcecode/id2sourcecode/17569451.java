    public ByteFileChannel(final String sourceFileName, final SampleSign sign) throws IOException {
        this.file = new RandomAccessFile(sourceFileName, "r");
        this.channel = file.getChannel();
        this.sign = sign;
    }
