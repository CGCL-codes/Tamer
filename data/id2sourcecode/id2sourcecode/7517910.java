    public MmapEventStore(String fileName) throws IOException {
        final File file = new File(fileName);
        final RandomAccessFile raFile = new RandomAccessFile(file, "rw");
        final FileChannel channel = raFile.getChannel();
        this.mappedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, (int) channel.size());
        this.buffer = this.mappedBuffer.asIntBuffer();
        IntBufferMatrix matrix = new IntBufferMatrix(this.buffer, (short) 10, (short) 10);
    }
