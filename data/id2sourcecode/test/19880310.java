    private static String read(InputStream stream) throws IOException {
        ReadableByteChannel channel = Channels.newChannel(stream);
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        byte[] array = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = channel.read(buf)) >= 0) {
            buf.rewind();
            buf.get(array, 0, read);
            baos.write(array, 0, read);
            buf.rewind();
        }
        baos.close();
        channel.close();
        return new StringBuilder(baos.toString("UTF-8")).toString();
    }
