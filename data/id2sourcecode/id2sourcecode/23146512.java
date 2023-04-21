    public static long copy(final InputStream in, final OutputStream out) throws IOException {
        long length = 0;
        final byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
            length += read;
        }
        return length;
    }
