    protected void copy(InputStream _in, OutputStream _out) throws IOException {
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = _in.read(buf)) > 0) _out.write(buf, 0, len);
    }
