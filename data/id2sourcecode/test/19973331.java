    public static void copy(InputStream in, OutputStream out) throws IOException {
        int read;
        byte[] buf = new byte[1000];
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
    }
