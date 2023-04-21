    protected void readToLimitFrom(final InputStream is, final long limit, final byte[] b) throws IOException {
        int read = b.length;
        int total = 0;
        while ((read = is.read(b, 0, Math.min(b.length, (int) (limit - total)))) != -1 && total < limit) {
            total += read;
            write(b, 0, read);
        }
        if (total != limit) {
            throw new IOException("Read " + total + " but expected " + limit);
        }
    }
