    public static void pipe(InputStream in, OutputStream out, boolean isBlocking, ByteFilter filter) throws IOException {
        byte[] buf = new byte[MAX_BUFFER_SIZE];
        int nread;
        int navailable;
        int total = 0;
        synchronized (in) {
            while ((navailable = isBlocking ? buf.length : in.available()) > 0 && (nread = in.read(buf, 0, Math.min(buf.length, navailable))) >= 0) {
                if (filter == null) {
                    out.write(buf, 0, nread);
                } else {
                    byte[] filtered = filter.filter(buf, nread);
                    out.write(filtered);
                }
                total += nread;
            }
        }
        out.flush();
        buf = null;
    }
