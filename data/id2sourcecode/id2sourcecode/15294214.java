    public final void setWriteSize(int writeSize) {
        if (writeSize < 0) {
            throw new IllegalArgumentException("writeSize < 0");
        }
        if (this.writeSize >= 0) {
            throw new UnsupportedOperationException("writeSize already set");
        }
        this.writeSize = writeSize;
    }
