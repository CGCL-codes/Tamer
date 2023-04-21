    @Override
    public void write(int val) throws IOException {
        if (!isOpen()) throw new IOException("write(" + val + ") not open");
        if (null == this.out) throw new IOException("write(" + val + ") real stream already closed");
        this.out.write(val);
    }
