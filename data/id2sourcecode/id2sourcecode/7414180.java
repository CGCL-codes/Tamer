    public static void flush(InputStream in, OutputStream out, byte[] chunk) throws IOException {
        int readLen = -1;
        while ((readLen = in.read(chunk)) != -1) {
            out.write(chunk, 0, readLen);
        }
        out.flush();
    }
