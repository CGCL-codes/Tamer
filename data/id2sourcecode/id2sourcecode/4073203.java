    @Override
    public void write(byte b[], int off, int len) {
        getOutputStreamForCurrentThread().write(b, off, len);
    }
