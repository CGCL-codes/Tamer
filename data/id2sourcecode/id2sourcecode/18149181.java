    protected void openForWrite() throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "rws");
        channel = randomAccessFile.getChannel();
        fileByteBuffer = ByteBuffer.allocate(64 * 1024);
    }
