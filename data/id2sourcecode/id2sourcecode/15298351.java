    private static void jbwrite(InputStream _in, OutputStream _out) throws IOException {
        int b;
        while ((b = _in.read()) != -1) _out.write((byte) b);
        _out.flush();
    }
