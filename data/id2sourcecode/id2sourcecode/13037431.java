    protected MappedByteBuffer getMappedBuffer(RandomAccessFile pRaf) throws IOException {
        FileChannel channel = pRaf.getChannel();
        int fileLength = (int) channel.size();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
        return buffer;
    }
