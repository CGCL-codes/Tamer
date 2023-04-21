    public ShortFileChannel(final File file, final SampleSign sign, final ByteOrder order) throws IOException {
        this.file = new RandomAccessFile(file, "r");
        if ((this.file.length() % 2) != 0) {
            this.file.close();
            throw new IOException("File length does not match");
        }
        this.channel = this.file.getChannel();
        this.sign = sign;
        this.order = order;
    }
