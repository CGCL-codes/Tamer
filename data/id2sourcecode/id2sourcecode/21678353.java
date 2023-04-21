    private void copy(InputStream src, OutputStream dst) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = src.read(buf)) > 0) dst.write(buf, 0, len);
        src.close();
        dst.close();
    }
