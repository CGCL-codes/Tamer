    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[COPY_BUF_SIZE];
        int read = 0;
        while ((read = is.read(buf)) != -1) {
            os.write(buf, 0, read);
        }
    }
