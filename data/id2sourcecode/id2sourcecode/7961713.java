    public static byte[] readFully(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        for (int readBytes = in.read(buffer); readBytes != -1; readBytes = in.read(buffer)) {
            out.write(buffer, 0, readBytes);
        }
        return out.toByteArray();
    }
