    public ByteFileChannel(final File file, final SampleSign sign) throws IOException {
        this.file = new RandomAccessFile(file, "r");
        this.channel = this.file.getChannel();
        this.sign = sign;
    }
