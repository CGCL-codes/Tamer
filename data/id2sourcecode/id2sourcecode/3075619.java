    public static long copy(InputStream in, OutputStream out) throws IOException {
        if ((in == null) || (out == null)) return 0;
        long total = 0;
        int read = 0;
        byte[] data = new byte[8192];
        while ((read = in.read(data)) >= 0) {
            out.write(data, 0, read);
            total += read;
        }
        return total;
    }
