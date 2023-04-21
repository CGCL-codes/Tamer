    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }
    }
