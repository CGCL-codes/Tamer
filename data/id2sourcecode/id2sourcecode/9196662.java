    public static void copy(InputStream src, OutputStream dst, long count) throws IOException {
        byte[] buff = new byte[BUFFER_SIZE];
        while (count > 0) {
            int toread = count > BUFFER_SIZE ? BUFFER_SIZE : (int) count;
            int readcount = src.read(buff, 0, toread);
            dst.write(buff, 0, readcount);
            count -= readcount;
        }
    }
