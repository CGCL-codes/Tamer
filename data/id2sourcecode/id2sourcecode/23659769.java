    public static final int copy(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        int count = 0, read;
        byte[] iobuf = new byte[COPY];
        while (0 < (read = in.read(iobuf, 0, COPY))) {
            out.write(iobuf, 0, read);
            count += read;
        }
        return count;
    }
