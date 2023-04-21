    private static byte[] getClassBytes(final Class<?> clazz) throws IOException {
        final URL loc = resolveClassBytesLocation(clazz);
        final InputStream in = (loc == null) ? null : loc.openStream();
        if (in == null) throw new FileNotFoundException("Cannot resolve location of " + ((clazz == null) ? null : clazz.getName()));
        final int COPY_SIZE = 4096;
        final ByteArrayOutputStream out = new ByteArrayOutputStream(COPY_SIZE);
        final byte[] buf = new byte[COPY_SIZE];
        try {
            for (int readLen = in.read(buf); readLen >= 0; readLen = in.read(buf)) out.write(buf, 0, readLen);
        } finally {
            in.close();
        }
        return out.toByteArray();
    }
