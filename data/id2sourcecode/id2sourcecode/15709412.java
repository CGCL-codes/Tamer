    private static void transfer(InputStream in, FileOutputStream fileOutputStream) throws IOException {
        ReadableByteChannel ic = Channels.newChannel(in);
        FileChannel oc = fileOutputStream.getChannel();
        ByteBuffer buf = ByteBuffer.allocateDirect(10000);
        int read = ic.read(buf);
        while (read > -1) {
            buf.flip();
            oc.write(buf);
            buf.flip();
            read = ic.read(buf);
        }
        ic.close();
        oc.close();
    }
