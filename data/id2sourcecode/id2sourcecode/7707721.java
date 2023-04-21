    public static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int read = 0;
        do {
            read = in.read(buf);
            out.write(buf, 0, read);
        } while (read > 0);
        out.flush();
        out.close();
        return (out.toByteArray());
    }
