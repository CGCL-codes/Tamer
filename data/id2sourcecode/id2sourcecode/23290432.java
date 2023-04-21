    public static void copyStreams(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
        int n = 0;
        final byte[] buffer = new byte[bufferSize];
        while (-1 != (n = input.read(buffer))) output.write(buffer, 0, n);
    }
