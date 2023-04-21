    public static void writeAll(InputStream in, OutputStream out, byte[] b) throws IOException {
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
