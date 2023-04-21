    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] data = new byte[1024];
        int read;
        while ((read = in.read(data)) > -1) {
            out.write(data, 0, read);
        }
    }
