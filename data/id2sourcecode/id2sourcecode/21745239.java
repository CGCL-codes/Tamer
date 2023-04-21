    protected synchronized int read(byte[] data, int offset, int len) throws IOException {
        try {
            block();
        } catch (InterruptedException ex) {
            throw new InterruptedIOException("The blocking operation was interrupted");
        }
        if (closed && (available() <= 0)) {
            return -1;
        }
        int read = (len > (writepos - readpos)) ? (writepos - readpos) : len;
        System.arraycopy(buf, readpos, data, offset, read);
        readpos += read;
        return read;
    }
