    public static void write(InputStream in, OutputStream out, int length) throws IOException {
        byte[] data = new byte[10000];
        while (length > 0) {
            int read = in.read(data, 0, Math.min(data.length, length));
            out.write(data, 0, read);
            length -= read;
        }
    }
