    public static void loadFromFileNew(File src, OutputStream dst) throws IOException {
        java.io.FileInputStream fis = new java.io.FileInputStream(src);
        java.nio.channels.WritableByteChannel wbc = java.nio.channels.Channels.newChannel(dst);
        fis.getChannel().transferTo(0, fis.available(), wbc);
        fis.close();
    }
