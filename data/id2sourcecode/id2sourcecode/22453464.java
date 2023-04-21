    public int read() throws IOException {
        int read = in.read();
        if (out != null && read > 0) out.write(read);
        return read;
    }
