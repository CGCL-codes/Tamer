    public static final byte[] loadResource(InputStream stream) throws IOException {
        if (stream != null) {
            final int BUFFER_SIZE = 4096;
            final BufferedInputStream input = new BufferedInputStream(stream);
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final byte[] reader = new byte[BUFFER_SIZE];
            int r = 0;
            while ((r = input.read(reader, 0, BUFFER_SIZE)) != -1) buffer.write(reader, 0, r);
            return buffer.toByteArray();
        }
        return null;
    }
