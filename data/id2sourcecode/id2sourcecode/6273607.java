    public static void write(InputStream in, OutputStream out, boolean closeBoth) throws IOException {
        byte[] buff = new byte[1024];
        int read = 0;
        while ((read = in.read(buff)) > 0) {
            out.write(buff, 0, read);
        }
        out.flush();
        if (closeBoth) {
            safeCloseStreams(in, out);
        }
    }
