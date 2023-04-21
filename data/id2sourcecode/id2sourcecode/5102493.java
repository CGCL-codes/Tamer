    protected void copyFileContents(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFSIZ];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
    }
