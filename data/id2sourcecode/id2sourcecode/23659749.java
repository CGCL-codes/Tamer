    public Bbuf readFrom(java.io.InputStream in) throws java.io.IOException {
        byte[] iobuf = new byte[COPY];
        int count;
        while (0 < (count = in.read(iobuf, 0, COPY))) this.write(iobuf, 0, count);
        return this;
    }
