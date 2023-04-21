    private byte[] readFrom(File src) throws IOException {
        long srcsize = src.length();
        if (srcsize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        FileInputStream inputStream = new FileInputStream(src);
        FileChannel fileChannel = inputStream.getChannel();
        byte[] array = new byte[(int) srcsize];
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        int read = 0;
        while (read < srcsize) {
            read += fileChannel.read(byteBuffer);
        }
        fileChannel.close();
        return array;
    }
