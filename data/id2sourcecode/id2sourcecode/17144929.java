    public int read() throws IOException {
        if (in == null) throw new IllegalStateException("This binary is in write mode! Can't execute read()!");
        int res = in.read();
        if (res >= 0) ++bytesRead;
        return res;
    }
