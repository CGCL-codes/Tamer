    public int read(byte[] b, int off, int len) throws IOException {
        int read = in.read(b, off, len);
        if (out != null && read > 0) out.write(b, off, read);
        return read;
    }
