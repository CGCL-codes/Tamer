    public static void copy(InputStream src, OutputStream dest) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = src.read(buffer)) >= 0) {
            dest.write(buffer, 0, read);
        }
    }
