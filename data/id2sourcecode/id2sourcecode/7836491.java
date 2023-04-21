    public void flush(final boolean close) throws IOException {
        if (!dirty) {
            return;
        }
        if (!open) {
            throw new IllegalStateException();
        }
        raf.getChannel().force(true);
        this.dirty = false;
        if (close) {
            close();
        }
    }
