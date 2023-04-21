    private void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[500000];
        int nread;
        synchronized (in) {
            while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, nread);
            }
        }
        out.flush();
        in.close();
        buf = null;
    }
