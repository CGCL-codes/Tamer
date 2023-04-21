    public static void checkWriteSpace(FileOutputStream aStream, ByteBuffer aBuf) throws IOException {
        if (aBuf.remaining() < 8) {
            aBuf.flip();
            aStream.getChannel().write(aBuf);
            aBuf.flip();
        }
    }
