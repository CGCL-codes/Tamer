    @Override
    public void write(int b) {
        getOutputStreamForCurrentThread().write(b);
    }
