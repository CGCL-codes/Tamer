    public static void copyFully(InputStream _in, OutputStream _out) throws IOException {
        byte[] buffer = null;
        try {
            buffer = FuFactoryByteArray.get(32768, -1, false);
            final int lb = buffer.length;
            int nr;
            while ((nr = _in.read(buffer, 0, lb)) >= 0) _out.write(buffer, 0, nr);
        } finally {
            FuFactoryByteArray.release(buffer);
        }
    }
