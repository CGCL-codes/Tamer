    private void allocateFile(int size, File f) throws IOException {
        int bufCount = size / FILE_BLOCKSIZE;
        size = bufCount * FILE_BLOCKSIZE;
        RandomAccessFile file = new RandomAccessFile(f, "rw");
        try {
            file.setLength(size);
            ByteBuffer fileBuf = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, size);
            sliceBuffer(fileBuf, FILE_BLOCKSIZE, fileBuffers);
        } finally {
            file.close();
        }
    }
