    @Override
    public void flush() throws IOException {
        raf.getChannel().force(false);
    }
