    private static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
