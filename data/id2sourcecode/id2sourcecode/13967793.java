    public static void copy(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
        out.flush();
    }
