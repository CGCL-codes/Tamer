    public final void write(final byte[] buf, final int bufOffset, final int length) throws IOException {
        if (this.readOnlyMode) throw new IOException("Cannot write in read only mode!");
        currentFilePosition = -currentFilePosition;
        this.file.write(buf, bufOffset, length);
        currentFilePosition = (-currentFilePosition) + length;
        this.lastWrite = System.currentTimeMillis();
    }
