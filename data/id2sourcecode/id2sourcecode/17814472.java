    private int gunzip(GZIPInputStream zis, OutputStream out, List<XValue> args) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = zis.read(buf)) > 0) out.write(buf, 0, len);
        zis.close();
        out.close();
        return 0;
    }
