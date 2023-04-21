    public static void pipe(InputStream in, OutputStream out, boolean isBlocking, ByteFilter filter) throws IOException {
        byte[] buf = new byte[50000];
        int nread;
        int navailable;
        int total = 0;
        synchronized (in) {
            navailable = isBlocking ? buf.length : in.available();
            nread = in.read(buf, 0, Math.min(buf.length, navailable));
            while (navailable > 0 && nread >= 0) {
                if (filter == null) {
                    out.write(buf, 0, nread);
                } else {
                    byte[] filtered = filter.filter(buf, nread);
                    out.write(filtered);
                }
                total += nread;
                navailable = isBlocking ? buf.length : in.available();
                nread = in.read(buf, 0, Math.min(buf.length, navailable));
            }
        }
        out.flush();
        buf = null;
    }
