    static File writeToTempFile(ByteBuffer buffer, ReadableByteChannel channel) throws IOException {
        File tempFile = java.io.File.createTempFile("droid", null);
        FileChannel fc = (new FileOutputStream(tempFile)).getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8192);
        fc.write(buffer);
        buf.clear();
        for (; ; ) {
            if (channel.read(buf) < 0) {
                break;
            }
            buf.flip();
            fc.write(buf);
            buf.compact();
        }
        fc.close();
        return tempFile;
    }
