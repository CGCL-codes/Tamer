    public static long copy(InputStream src, OutputStream dst) throws IOException {
        byte[] buff = new byte[BUFFER_SIZE];
        long count = 0L;
        int readcount;
        while ((readcount = src.read(buff)) > -1) {
            dst.write(buff, 0, readcount);
            count += readcount;
        }
        return count;
    }
