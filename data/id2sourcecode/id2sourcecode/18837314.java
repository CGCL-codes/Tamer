    private static void readFully(final InputStream in, final ByteArrayOStream out, final byte[] buf) throws IOException {
        for (int read; (read = in.read(buf)) >= 0; ) {
            out.write(buf, 0, read);
        }
    }
