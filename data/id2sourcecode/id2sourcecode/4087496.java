    protected void readFullyFrom(final InputStream is, final long recordLength, final byte[] b) throws IOException {
        int read = b.length;
        int total = 0;
        while ((read = is.read(b)) != -1 && total < recordLength) {
            total += read;
            write(b, 0, read);
        }
        if (total != recordLength) {
            throw new IOException("Read " + total + " but expected " + recordLength);
        }
    }
