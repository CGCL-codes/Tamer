    public int read(byte[] b) throws IOException {
        int read = in.read(b);
        if (out != null && read > 0) out.write(b, 0, read);
        return read;
    }
