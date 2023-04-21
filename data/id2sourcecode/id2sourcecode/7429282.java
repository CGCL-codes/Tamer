    public static void writeFile(byte[] fileBuffer, String fullDestPath) throws Exception {
        ByteBuffer rBuf = ByteBuffer.allocate(fileBuffer.length);
        rBuf.put(fileBuffer);
        rBuf.rewind();
        WritableByteChannel channel = new FileOutputStream(fullDestPath).getChannel();
        channel.write(rBuf);
        channel.close();
    }
