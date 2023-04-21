    public static void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        int bread = 0;
        while (bread >= 0) {
            bread = in.read(buf);
            out.write(buf, 0, bread);
        }
    }
