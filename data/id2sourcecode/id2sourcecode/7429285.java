    public static byte[] readFile(String fullFilename) throws IOException {
        byte[] fileBuffer = null;
        FileChannel channel = new FileInputStream(fullFilename).getChannel();
        ByteBuffer rBuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
        fileBuffer = new byte[(int) channel.size()];
        rBuf.rewind();
        rBuf.get(fileBuffer);
        channel.close();
        return fileBuffer;
    }
