    public MappedFileChannel(RandomAccessFile raf, int length) throws IOException {
        this.raf = raf;
        buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
    }
