    public static void readFileFromStream(InputStream inStream, File outputFile, boolean closeStream) throws IOException {
        ReadableByteChannel srcChannel = Channels.newChannel(inStream);
        FileChannel dstChannel = new FileOutputStream(outputFile).getChannel();
        ByteBuffer byteBuff = ByteBuffer.allocateDirect(BUFFER_SIZE);
        int s = 0;
        while ((s = srcChannel.read(byteBuff)) != -1) {
            byteBuff.flip();
            dstChannel.write(byteBuff);
            if (byteBuff.hasRemaining()) byteBuff.compact(); else byteBuff.clear();
        }
        dstChannel.force(true);
        dstChannel.close();
        if (closeStream) {
            srcChannel.close();
        }
    }
