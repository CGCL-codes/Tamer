    public static void appendByteBufferToFile(File file, ByteBuffer bbuf) {
        try {
            bbuf.position(0);
            FileChannel wChannel = new FileOutputStream(file, true).getChannel();
            wChannel.write(bbuf);
            wChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }
