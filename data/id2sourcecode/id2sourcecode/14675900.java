    public static void send(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int c;
        while ((c = in.read(buffer)) > 0) out.write(buffer, 0, c);
    }
