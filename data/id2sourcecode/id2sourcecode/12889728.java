    public static void copyStream(InputStream _in, OutputStream _out, byte[] _buffer) throws IOException {
        int read = 0;
        byte[] buf = _buffer == null ? new byte[32768] : _buffer;
        final int bufSize = buf.length;
        while ((read = _in.read(buf, 0, bufSize)) >= 0) {
            if (read == 0) {
                Thread.yield();
            } else {
                _out.write(buf, 0, read);
            }
        }
        _out.flush();
    }
