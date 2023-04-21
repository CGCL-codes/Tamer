    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] block = new byte[1024];
        int read = 0;
        while ((read = in.read(block)) > -1) {
            out.write(block, 0, read);
        }
        out.flush();
    }
