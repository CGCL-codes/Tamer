    public void write(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        FileChannel ch = raf.getChannel();
        int fileLength = dataSize();
        raf.setLength(fileLength);
        MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
        write(buffer);
        buffer.force();
        ch.close();
    }
