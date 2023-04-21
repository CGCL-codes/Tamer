    public synchronized OutputStream getOutputStream() throws IOException {
        if (isReleased()) {
            throw new IOException("Attempt to use a released RandomAccessBucket: " + getName());
        }
        if (readOnly) {
            throw new IOException("Tried to write a read-only Bucket.");
        }
        final OutputStream newOut = new RAOutputStream(this, file.getAbsolutePath());
        streams.addElement(newOut);
        return newOut;
    }
