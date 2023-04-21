    public static File copyToFile(final InputStream is, final File file) throws IOException {
        FileOutputStream fos = null;
        ReadableByteChannel source = null;
        FileChannel target = null;
        try {
            fos = new FileOutputStream(file);
            source = Channels.newChannel(is);
            target = fos.getChannel();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
            while (source.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    target.write(buffer);
                }
                buffer.clear();
            }
        } finally {
            if (source != null) {
                source.close();
            }
            if (target != null) {
                target.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }
