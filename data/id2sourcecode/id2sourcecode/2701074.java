    public static int pipe(InputStream in, OutputStream out, int length, int limit) throws IOException {
        byte[] data = new byte[length];
        int total = 0, read = in.read(data);
        while (read > -1) {
            if (limit > 0 && total > limit) {
                throw new IOException("Max allowed bytes read. (" + limit + ")");
            }
            total += read;
            out.write(data, 0, read);
            read = in.read(data);
        }
        return total;
    }
