    public static void checkReadSpace(FileInputStream aStream, ByteBuffer aBuf) throws IOException {
        if (aBuf.remaining() < 8) {
            aBuf.flip();
            aStream.getChannel().read(aBuf);
            aBuf.flip();
        }
    }
