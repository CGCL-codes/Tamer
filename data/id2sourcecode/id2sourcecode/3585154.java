    public static void copy(InputStream resource, File to) throws IOException {
        FileChannel channel = (new FileOutputStream(to)).getChannel();
        byte[] bytes = loadBytes(resource);
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        channel.write(buf);
        channel.close();
    }
