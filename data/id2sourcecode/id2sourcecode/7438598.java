    @Override
    public void copyTo(OutputStream out, int length) throws IOException {
        if (th != Thread.currentThread()) throw new IllegalStateException("Entered by wrong thread");
        int remaining = length;
        while (remaining > 0) {
            if (isEOF()) throw new EOFException("remaining: " + remaining);
            int read = Math.min(remaining, pdvend - pos);
            out.write(buf, pos, read);
            remaining -= read;
            pos += read;
        }
    }
