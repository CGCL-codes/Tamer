    public static void copy(InputStream src, OutputStream dst) throws IOException {
        byte[] buffer = new byte[8192];
        while (true) {
            int read = src.read(buffer);
            if (read < 0) {
                return;
            } else if (read == 0) {
                continue;
            }
            dst.write(buffer, 0, read);
        }
    }
