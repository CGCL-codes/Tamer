    public bbuf(InputStream in) throws IOException {
        this(1024);
        if (null == in) throw new IllegalArgumentException("Null input stream for `bbuf'."); else {
            int buflen = 512, bytes;
            byte[] readbuf = new byte[buflen];
            while (0 < (bytes = in.read(readbuf, 0, buflen))) write(readbuf, 0, bytes);
        }
    }
