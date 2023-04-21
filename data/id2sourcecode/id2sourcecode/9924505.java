    public static long readTo(InputStream in, OutputStream out, long len, byte[] buf) throws IOException {
        long n = 0;
        int x = 0;
        while ((n += x) < len && (x = in.read(buf, 0, (int) Math.min(buf.length, len - n))) > 0) out.write(buf, 0, x);
        return n;
    }
