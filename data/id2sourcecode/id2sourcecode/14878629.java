    public static long bufStreamCopy(InputStream is, OutputStream os, byte[] b) throws IOException {
        long bytes = 0;
        int read = is.read(b, 0, b.length);
        while (read != -1) {
            os.write(b, 0, read);
            bytes += read;
            read = is.read(b, 0, b.length);
        }
        os.flush();
        return bytes;
    }
