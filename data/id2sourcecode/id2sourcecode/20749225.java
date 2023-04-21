    private static boolean contentsAreEqualSequential(File file, File other) throws IOException {
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        FileChannel otherChannel = new FileInputStream(other).getChannel();
        ByteBuffer fileBuf = ByteBuffer.allocate(SEQUENTIAL_BUF_SIZE);
        ByteBuffer otherBuf = ByteBuffer.allocate(SEQUENTIAL_BUF_SIZE);
        boolean success = true;
        while (true) {
            fileChannel.read(fileBuf);
            otherChannel.read(otherBuf);
            if (!(fileBuf.equals(otherBuf))) {
                success = false;
                break;
            }
            if (fileBuf.position() == 0) {
                break;
            }
            fileBuf.clear();
            otherBuf.clear();
        }
        fileChannel.close();
        otherChannel.close();
        return success;
    }
