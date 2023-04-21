    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (!isOpen()) throw new IOException("write(" + off + "/" + len + ") not open");
        if (null == this.out) throw new IOException("write(" + off + "/" + len + ") real stream already closed");
        this.out.write(b, off, len);
    }
