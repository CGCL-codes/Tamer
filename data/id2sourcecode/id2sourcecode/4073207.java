    @Override
    public void write(byte b[]) throws IOException {
        getOutputStreamForCurrentThread().write(b);
    }
