    protected void openForRead() throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "r");
        channel = randomAccessFile.getChannel();
        fileByteBuffer = ByteBuffer.allocate(4 * 1024);
    }
