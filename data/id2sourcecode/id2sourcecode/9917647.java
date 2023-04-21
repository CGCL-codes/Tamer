    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        long total = 0;
        byte[] buf = new byte[bufferSize];
        int read;
        while ((read = in.read(buf)) > -1) {
            out.write(buf, 0, read);
            total += read;
        }
        return total;
    }
