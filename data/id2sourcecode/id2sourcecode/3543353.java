    private static void send(InputStream in, OutputStream out) throws IOException {
        int c;
        byte[] buffer = new byte[2048];
        while ((c = in.read(buffer)) > 0) out.write(buffer, 0, c);
    }
