    public int read(byte[] b, int off, int len) throws IOException {
        int read = this.dataIn.read(b, off, len);
        this.dataOut.write(b, off, read);
        return read;
    }
