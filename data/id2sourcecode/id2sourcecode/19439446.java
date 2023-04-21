    public static void copy(InputStream _in, OutputStream _out) throws IOException {
        byte[] buf = new byte[8 * 1024];
        for (; ; ) {
            int read = _in.read(buf);
            if (read == -1) {
                break;
            }
            _out.write(buf, 0, read);
        }
    }
