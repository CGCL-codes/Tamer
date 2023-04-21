    private int gzip(InputStream is, GZIPOutputStream zos) throws IOException {
        int ret = 0;
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) zos.write(buf, 0, len);
        is.close();
        zos.close();
        return ret;
    }
