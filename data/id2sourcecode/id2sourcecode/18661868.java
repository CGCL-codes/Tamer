    public static boolean isEquals(File file, ByteBuffer[] buffers) throws IOException {
        int length = 0;
        for (ByteBuffer byteBuffer : buffers) {
            length += byteBuffer.remaining();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(length);
        fc.read(buf);
        buf.flip();
        boolean isEquals = isEquals(buf, buffers);
        fc.close();
        raf.close();
        return isEquals;
    }
