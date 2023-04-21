    public static byte[] readFile(String fullFilename, int position, int length) throws IOException {
        FileChannel channel = new FileInputStream(fullFilename).getChannel();
        int chunksize = 0;
        ByteBuffer rBuf = null;
        if (position == -1 && length == -1) {
            rBuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
            chunksize = (int) channel.size();
        } else {
            rBuf = channel.map(FileChannel.MapMode.READ_ONLY, position, length);
            chunksize = length;
        }
        byte[] fileBuffer = null;
        fileBuffer = new byte[chunksize];
        rBuf.rewind();
        rBuf.get(fileBuffer);
        channel.close();
        return fileBuffer;
    }
