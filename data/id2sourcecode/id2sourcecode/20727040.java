    @Override
    public int read() throws IOException {
        int read = wrapped.read();
        out.write(read);
        return read;
    }
