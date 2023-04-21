    public static boolean isEquals(File file, ByteBuffer[] buffers) throws IOException {
        int length = 0;
        for (ByteBuffer byteBuffer : buffers) {
            length += byteBuffer.remaining();
        }
        FileChannel fc = new RandomAccessFile(file, "r").getChannel();
        ByteBuffer buf = ByteBuffer.allocate(length);
        fc.read(buf);
        buf.flip();
        return isEquals(buf, buffers);
    }
