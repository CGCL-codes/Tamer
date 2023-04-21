    public static File getFileFromByteArray(File file, byte[] bytes, boolean append) {
        ByteBuffer bbuf = getByteBufferFromByteArray(bytes);
        try {
            FileChannel wChannel = new FileOutputStream(file, append).getChannel();
            wChannel.write(bbuf);
            wChannel.close();
        } catch (IOException e) {
        }
        return file;
    }
