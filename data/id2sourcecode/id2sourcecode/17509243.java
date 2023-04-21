    private static byte[] readFully(final InputStream in) throws IOException {
        final ByteArrayOutputStream buf1 = new ByteArrayOutputStream();
        final byte[] buf2 = new byte[8 * 1024];
        for (int read; (read = in.read(buf2)) > 0; ) {
            buf1.write(buf2, 0, read);
        }
        return buf1.toByteArray();
    }
