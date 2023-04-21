    public static int copy(InputStream in, OutputStream out, byte[] buffer) {
        try {
            int read = in.read(buffer);
            if (read > 0) {
                out.write(buffer, 0, read);
            }
            return read;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
