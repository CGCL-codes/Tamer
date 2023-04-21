    public boolean renameTo(File dest) throws IOException {
        if (dest == null) {
            throw new NullPointerException("dest");
        }
        if (channelBuffer == null) {
            dest.createNewFile();
            return true;
        }
        int length = channelBuffer.readableBytes();
        FileOutputStream outputStream = new FileOutputStream(dest);
        FileChannel fileChannel = outputStream.getChannel();
        ByteBuffer byteBuffer = channelBuffer.toByteBuffer();
        int written = 0;
        while (written < length) {
            written += fileChannel.write(byteBuffer);
            fileChannel.force(false);
        }
        fileChannel.close();
        return written == length;
    }
