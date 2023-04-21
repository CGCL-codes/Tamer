    public static void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf, 0, buf.length)) >= 0) out.write(buf, 0, n);
    }
