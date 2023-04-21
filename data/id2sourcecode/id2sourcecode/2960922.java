    static void pipe(InputStream in, OutputStream out, boolean isBlocking) throws IOException {
        int nread;
        int navailable;
        while ((navailable = isBlocking ? Integer.MAX_VALUE : in.available()) > 0 && (nread = in.read(buf, 0, Math.min(buf.length, navailable))) >= 0) {
            out.write(buf, 0, nread);
        }
        out.flush();
    }
