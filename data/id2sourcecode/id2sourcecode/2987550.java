    public static String read(File from) throws Exception {
        final FileInputStream istream = new FileInputStream(from);
        final FileChannel channel = istream.getChannel();
        final StringWriter writer = new StringWriter();
        final ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        int read = 0;
        while ((read = channel.read(buffer)) > 0) {
            writer.append(new String(buffer.array(), 0, read));
        }
        return writer.getBuffer().toString();
    }
