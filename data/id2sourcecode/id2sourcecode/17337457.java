    public MappedByteBufferAccessor(File f) {
        try {
            raf = new RandomAccessFile(f, "rw");
            channel = raf.getChannel();
            reopen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
